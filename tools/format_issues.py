#!/usr/bin/env python3

import json
import textwrap
from datetime import datetime
import os
import urllib.request
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

def make_request(what):
    request = urllib.request.Request(url="https://git.cs.kent.ac.uk/api/v3" + what)
    request.add_header("PRIVATE-TOKEN", private_token)
    return urllib.request.urlopen(request).read().decode()

process_output = make_request("/projects/97/issues")

data = json.loads(process_output)

print("Issues")
print("======")
print("      ")

for issue in reversed(data):
    real_id = issue["id"]
    closed_str = "[CLOSED]" if issue["state"] == "closed" else "[OPEN]"
    pretty_title = ("## Issue #" + str(issue["iid"]) + " - " + issue["title"] +
                    " " + closed_str)
    pretty_created_at = datetime.strptime(issue["created_at"], "%Y-%m-%dT%H:%M:%S.%fZ")
    pretty_created_at = pretty_created_at.strftime("%d/%m/%Y %H:%M")
    print(pretty_title)
    print("-" * len(pretty_title))
    print("**Opened by:** " + issue["author"]["name"] + " @ " + pretty_created_at)
    print("**Assigned to:** " + issue["assignee"]["name"])
    print("")
    for line in textwrap.wrap(issue["description"], 72):
        print(line)

    print("")
    print("### Comments")

    process_output = make_request("/projects/97/issues/" + str(real_id) + "/notes")

    comments_data = json.loads(process_output)
    for comment in reversed(comments_data):
        comment_date = comment["created_at"]
        comment_date = datetime.strptime(comment_date, "%Y-%m-%dT%H:%M:%S.%fZ")
        comment_date = comment_date.strftime("%d/%m/%Y %H:%M")
        print("On " + comment_date + " " + comment["author"]["name"] + " wrote:")
        comment_body = textwrap.wrap(comment["body"], 70)
        for line in comment_body:
            print("> " + line)
        print("")
