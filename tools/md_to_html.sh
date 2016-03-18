#!/usr/bin/env bash

title=$1

echo "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""
echo "  \"http://www.w3.org/TR/html4/loose.dtd\">"
echo "<html>"
echo "<head><title>$title</title></head>"
echo "<body>"

markdown2 --extras fenced-code-blocks

echo "</body>"
echo "</html>"
