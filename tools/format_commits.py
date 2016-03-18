#!/usr/bin/env python3

from format_common import make_request, get_json_list

import json
from datetime import datetime
import os
import sys, getopt

try:
    opts, args = getopt.getopt(sys.argv[1:], "t:", ["token="])
except getopt.GetoptError:
    print("Usage: " + sys.argv[0] + " --token PRIVATE-TOKEN")
    sys.exit(2)

private_token = None
for o, a in opts:
    if o in ("-t", "--token"):
        private_token = a

if private_token is None:
    print("Usage: " + sys.argv[0] + " --token PRIVATE-TOKEN")
    sys.exit(2)

# GitLab's API is just horrendous, it talks about using the
# X-Next-Page header in the HTTP response to return the next page, but
# it just doesn't do that here even though there are other pages. Also
# pages start at 0 here but 1 everywhere else.
def get_commit_json_list():
    joined_data = []
    next_page = 0
    while True:
        output, _ = make_request("/projects/97/repository/commits", private_token, next_page, "")
        data = json.loads(output)
        joined_data = joined_data + data
        if len(data) == 0:
            # No more pages, break
            break;
        next_page += 1

    return joined_data

data = get_commit_json_list()

print("Commits")
print("=======")
print("")


for commit in reversed(data):
    commit_sha = commit["id"];
    pretty_title = ("Commit " + str(commit["id"]))
    pretty_created_at = datetime.strptime(commit["created_at"][:-6], "%Y-%m-%dT%H:%M:%S.%f")
    pretty_created_at = pretty_created_at.strftime("%d/%m/%Y %H:%M")

    print(pretty_title);
    print("-" * len(pretty_title))
    print("**Authored by:** " + commit["author_name"] + " <" + commit["author_email"] + ">")
    print("")
    print(pretty_created_at)

    print("");
    print("### Message")
    for line in commit["message"].splitlines():
        print(line)

    print("");

    comments_data = get_json_list("/projects/97/repository/commits/" + str(commit_sha) + "/comments", private_token)
    if len(comments_data) != 0:
        print("### Comments")
    # Unlike with merge requests and issues, the comments are sorted differently.
    for comment in comments_data:
        comment_date = comment["created_at"]
        comment_date = datetime.strptime(comment_date, "%Y-%m-%dT%H:%M:%S.%fZ")
        comment_date = comment_date.strftime("%d/%m/%Y %H:%M")
        print("On " + comment_date + " " + comment["author"]["name"] + " wrote:")
        comment_body = comment["note"].splitlines()
        for line in comment_body:
            print("> " + line)
        print("")
