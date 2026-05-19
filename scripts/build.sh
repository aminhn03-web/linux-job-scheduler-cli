#!/bin/bash

# Use Bash, create an output folder, compile all Java files from src, and place
# the compiled files into out

mkdir -p out

javac -d out src/*.java

if [ $? -eq 0 ]; then
	echo "Build successful."
else
    	echo "Build failed."
fi
