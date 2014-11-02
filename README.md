# OkelyDokuly: a Sudoku solver

OkelyDokuly is a command-line Sudoku puzzle solver written in Java.


## Usage

Sudoku format:

## Example puzzles

A selection of example Sudoku puzzles can be found in the `examples` directory. Files named `sd_ex*.csv` are valid (but incomplete) puzzles. 

Files named `sd_bad*.csv` are unsolvable in some way, either because they are unsatisfiable (ambiguous assignments) or because the grid does not meet the constraints of a valid Sudoku puzzle. These are included for testing purposes.

## Design

-

## How it works

Represented as a Constraint Satisfaction Problem (CSP) and solved using 
backtracking search with forwarding checking and constraint propagation. 
Wikiepdia page [...]. 

For anyone interested in CSPs in general, I'd recommend the excellent Artificial
Intelligence a Modern Approach by Stuart Russell and Peter Norvig. Chapter 6 of
the third edition is dedicated to constraint satisfaction problems, and even 
includes a discussion of modelling Sudoku as a CSP.

For anyone wishing to build on this codebase, inline documentation has been written with Javadoc. See `doc` directory for the typet HTML documentation.

Code written following Google's [Java style guide](http://google-styleguide.googlecode.com/svn/trunk/javaguide.html).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%