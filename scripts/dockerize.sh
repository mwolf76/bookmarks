#!/bin/sh
lein uberjar && docker build -t bookmarks:latest -f Dockerfile .

