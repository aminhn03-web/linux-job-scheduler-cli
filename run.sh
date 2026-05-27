#!/bin/bash

# Build the Java project before running it.
./scripts/build.sh

# Only run the program if the build succeeds.
if [ $? -eq 0 ]; then

    # Locate the SQLite JDBC driver and SLF4J logging libraries.
    SQLITE_JAR=$(ls /usr/share/java/sqlite-jdbc.jar /usr/share/java/xerial-sqlite-jdbc.jar 2>/dev/null | head -n 1)
    SLF4J_API=$(ls /usr/share/java/slf4j-api.jar 2>/dev/null | head -n 1)
    SLF4J_SIMPLE=$(ls /usr/share/java/slf4j-simple.jar 2>/dev/null | head -n 1)

    # Run the program with compiled classes, SQLite support, and logging libraries available.
    java -cp "out:$SQLITE_JAR:$SLF4J_API:$SLF4J_SIMPLE" Main "$@"
fi