#!/bin/bash
DATABASE_DIR=`pwd`/db
DATABASE_NAME="bookmarks"

DATABASE_URL=jdbc:h2:`pwd`/db/"$DATABASE_NAME"
export DATABASE_URL

if [ ! -d "$DATABASE_DIR" ]; then
	echo "Initializing db..."
	mkdir -p "$DATABASE_DIR"
	lein migratus
else
	echo "db sub-directory exists. Skipping..."
fi
