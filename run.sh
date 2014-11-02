#!/bin/bash
# Run the solver on example Sudoku grids.

#
# Params
classroot=bin
appclass=net.mattjw.okelydokuly.Solver

egdir=examples
solvdir=$egdir/solved

#
# Show the help
echo "\nDisplaying the help..."
java -cp $classroot $appclass --help

#
# Run one example
echo "\nRunning one example...\n"
java -cp $classroot $appclass $egdir/sd_ex0.csv
java -cp $classroot $appclass $egdir/sd_ex0.csv $egdir/solved/sd_ex0s.csv

#
# Run all examples
echo "\nAutomatically running all examples...\n"
rm -ri ./$solvdir
mkdir -p ./$solvdir

for fpth in $egdir/*.csv
do
    f=`basename $fpth`
    echo "[solving: $f]"
    java -cp $classroot $appclass $egdir/$f
    java -cp $classroot $appclass $egdir/$f $solvdir/$f
    echo
done