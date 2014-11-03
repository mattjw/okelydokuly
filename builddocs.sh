#!/bin/bash

#
# Params
dest=doc
src=src
packageroot=net

#
# Ensure docs destination exists, and clean it
echo "cleaning..."
mkdir -p ./$dest
rm -ri ./$dest/*

#
# Build the codebase's Javadoc
echo "building..."
javadoc -d ./$dest -sourcepath ./$src -subpackages $packageroot

echo "done!"

