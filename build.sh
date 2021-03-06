#!/bin/bash
#
# Compile the Sudoku solver source code.

#
# Params
dest=bin
appsrc=src/net/mattjw/okelydokuly/*.java

#
# Ensure build destination exists, and clean it
echo "cleaning..."
mkdir -p ./$dest
rm -ri ./$dest/*

#
# Compile
echo "compiling..."
javac -d $dest -cp $dest $appsrc

echo "done!"