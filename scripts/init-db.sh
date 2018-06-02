#!/bin/bash
DATABASE_DIR=`pwd`/db
DATABASE_NAME="bookmarks"

if [ ! -d "$DATABASE_DIR" ]; then
	echo "Initializing db..."
	DATABASE_URL=jdbc:h2:`pwd`/db/"$DATABASE_NAME" lein migratus
else
	echo "db sub-directory exists. Skipping..."
fi


