#!/bin/bash
. scripts/init-db.sh

DATABASE_URL=jdbc:h2:/var/db/"$DATABASE_NAME"
echo "using db $DATABASE_URL"

&>/dev/null docker run 			\
  --detach				\
  --env DATABASE_URL="$DATABASE_URL"	\
  --publish 3000:3000 			\
  --volume `pwd`/db:/var/db		\
  --name=bookmarks 			\
bookmarks:latest

