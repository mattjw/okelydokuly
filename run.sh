#!/bin/bash
# Run the solver on example Sudoku grids.

#
# Params
classroot=bin
appclass=net.mattjw.okelydokuly.Solver

egdir=examples

#
# Run examples
java -cp $classroot $appclass $egdir/sd_ex1.csv && echo
java -cp $classroot $appclass $egdir/sd_ex2.csv && echo
java -cp $classroot $appclass $egdir/sd_ex3.csv && echo
java -cp $classroot $appclass $egdir/sd_ex4.csv && echo
