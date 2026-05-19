#!/bin/bash

# This script builds the Java project. If the build suceeds, it runs Main and passes
# along whatever command you typed after ./run.sh

./scripts/build.sh

if [ $? -eq 0 ]; then
	java -cp out Main "$@"
fi
