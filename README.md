# bookmarks

generated using Luminus version "2.9.11.89"

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

This application uses H2 database engine. To function properly,
the environment variable DATABASE_URL must be set to valid JDBC
connection string.

    export DATABASE_URL=jdbc:h2:`pwd`/db/bookmarks

Before running for the first time, the database must be initialized.

    lein migratus

To start a web server for the application, run:

    lein run 

## License

Copyright © 2017-2018 Marco Pensallorto
