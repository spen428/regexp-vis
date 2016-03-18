#!/usr/bin/env python3

from format_common import get_json_list

import textwrap
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

data = get_json_list("/projects/97/merge_requests", private_token)

print("Merge Requests")
print("==============")
print("")

for req in reversed(data):
    real_id = req["id"];
    # Python could do well with pattern matching (a la Rust)
    status_str = {
        "closed" : "[CLOSED]",
        "opened" : "[OPEN]",
        "merged" : "[MERGED]",
    }.get(req["state"])
    pretty_title = ("Merge Request #" + str(req["iid"]) + " - " + req["title"] +
                    " " + status_str)
    pretty_created_at = datetime.strptime(req["created_at"], "%Y-%m-%dT%H:%M:%S.%fZ")
    pretty_created_at = pretty_created_at.strftime("%d/%m/%Y %H:%M")

    print(pretty_title);
    print("-" * len(pretty_title))
    print("**Opened by:** " + req["author"]["name"])
    print("")
    if not (req["assignee"] is None):
        print("**Assigned to:** " + req["assignee"]["name"])
    else:
        print("**Assigned to:** None")
    print("")
    print(pretty_created_at)
    print("");
    for line in textwrap.wrap(req["description"], 80):
        print(line)

    print("");
    print("### Comments")

    comments_data = get_json_list("/projects/97/merge_requests/" + str(real_id) + "/notes", private_token)
    for comment in reversed(comments_data):
        comment_date = comment["created_at"]
        comment_date = datetime.strptime(comment_date, "%Y-%m-%dT%H:%M:%S.%fZ")
        comment_date = comment_date.strftime("%d/%m/%Y %H:%M")
        print("On " + comment_date + " " + comment["author"]["name"] + " wrote:")
        comment_body = textwrap.wrap(comment["body"], 78)
        for line in comment_body:
            print("> " + line)
        print("")
