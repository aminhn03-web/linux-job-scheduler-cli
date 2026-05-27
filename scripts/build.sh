#!/bin/bash

# Create the output folder for compiled Java class files.
mkdir -p out

# Locate the SQLite JDBC driver and SLF4J logging libraries.
SQLITE_JAR=$(ls /usr/share/java/sqlite-jdbc.jar /usr/share/java/xerial-sqlite-jdbc.jar 2>/dev/null | head -n 1)
SLF4J_API=$(ls /usr/share/java/slf4j-api.jar 2>/dev/null | head -n 1)
SLF4J_SIMPLE=$(ls /usr/share/java/slf4j-simple.jar 2>/dev/null | head -n 1)

# Compile all Java source files with the required database and logging libraries.
javac -cp "$SQLITE_JAR:$SLF4J_API:$SLF4J_SIMPLE" -d out src/*.java

# Report whether compilation succeeded.
if [ $? -eq 0 ]; then
    echo "Build successful."
else
    echo "Build failed."
    exit 1
fi