import json
import urllib.request

def make_request(what, private_token, page, extra):
    request = urllib.request.Request(url="https://git.cs.kent.ac.uk/api/v3" + what + "?page=" + str(page) + extra)
    request.add_header("PRIVATE-TOKEN", private_token)

    response = urllib.request.urlopen(request)
    next_page = response.getheader("X-Next-Page")
    if next_page == "":
        next_page = None
    output = urllib.request.urlopen(request).read().decode()
    return (output, next_page)

def get_json_list(what, private_token):
    joined_data = []
    next_page = 1
    while not (next_page is None):
        output, next_page = make_request(what, private_token, next_page, "")
        data = json.loads(output)
        joined_data = joined_data + data

    return joined_data
