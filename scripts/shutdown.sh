#!/bin/bash
&> /dev/null docker stop bookmarks
&> /dev/null docker rm bookmarks && echo "Ok"
