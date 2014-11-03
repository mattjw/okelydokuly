# OkelyDokuly: a Sudoku solver

OkelyDokuly is a command-line Sudoku puzzle solver written in Java.


## Introduction

This repository includes source code, compiled class files, example Sudoku 
puzzles, and class documenation (exported to HTML via Javadoc).
Information on each of these components can be found in this readme.

To start solving Sudoku puzzles right away, the compiled class files 
can be found in the `bin` directory. To solve one of the example Sudoku puzzles,
open your terminal at the `bin` directory and try the following:

```
$ java net.mattjw.okelydokuly.Solver ../examples/sd_ex0.csv
1,3,5,2,9,7,8,6,4
9,8,2,4,1,6,7,5,3
7,6,4,3,8,5,1,9,2
2,1,8,7,3,9,6,4,5
5,9,7,8,6,4,2,3,1
6,4,3,1,5,2,9,7,8
4,2,6,5,7,1,3,8,9
3,5,9,6,2,8,4,1,7
8,7,1,9,4,3,5,2,6
```

As demonstrated above, this displays the solution on the command line. To instead save
the solution to a CSV file in the current directory:

```
$ java net.mattjw.okelydokuly.Solver ../examples/sd_ex0.csv sd_ex0_sol.csv
```

To display usage information, use the `--help` flag:

```
$ java net.mattjw.okelydokuly.Solver --help

okelydokuly: a Sudoku puzzle solver

Usage: 
  Solver [--help] <infile> [<outfile>]

  Solves the Sudoku in <infile> and saves the solution to <outfile>. If the output
  file is omitted, the solution is printed to the command line.

  Sudoku files are read and written in plain-text 9x9 comma-separated values
  format. A blank cell is represented by a 0.

Options:
  --help    Display this help prompt.
```

OkelyDokuly was written and tested to run with Java SE 7 and 8. The application has
no third-party dependencies. If you don't have the Java runtime environment, visit 
[Oracle](http://www.oracle.com/technetwork/java/javase) to download
it. If you'd like to compile from scratch, you'll also need to Java Development Kit.
Instructions to compile OkelyDokuly can be found later in this readme.

## Sudoku File Format

Sudoku puzzles are represented in plain text as a 9x9 grid of comma-separated
cells. Each row of the grid should appear on a new line. Each cell on a line
should be separated by a comma. A blank Sudoku cell should be represented by a
`0`.

Here is an example unsolved grid in this format:

```
0,6,3,4,9,0,0,0,1
0,0,0,0,0,0,7,0,9
0,1,9,0,0,0,0,0,0
0,0,1,0,0,2,9,3,0
9,0,0,1,0,7,0,0,2
0,7,8,9,0,0,4,0,0
0,0,0,0,0,0,8,2,0
3,0,6,0,0,0,0,0,0
4,0,0,0,2,9,1,7,0
```

Whitespace around cells (spaces and tabs) and at the beginning and end of each line 
is also permitted.

### Example puzzles

A selection of example Sudoku puzzles can be found in the `examples` directory. 
Files named `sd_ex*.csv` are valid (but not yet solved) puzzles. Files named
`sd_bad*.csv` are unsolvable or malformed in some way. These are included for
testing purposes.


## Building from Source

Source files are located in the `src` directory. You may want to build these 
yourself. The application has been tested to compile with both JDK 7 and JDK 8.
There are no third-party dependencies. To download and install the JDK for
your platform please refer to [Oracle's](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) instructions.

Here are three ways to build the project.

### 1. Directly Using `javac`

To build manually, change directory to the repository root (the directory above
`src`) and execute the following:

```
$ javac -d bin -cp bin src/*/*/*/*.java
```

This will compile from `src` and output the package hierarchy and class files to
the `bin` directory. Once compiled, the solver can be run according to the
instructions at the start of the readme. 

It can also be useful to explicitly state the classpath (using the `-cp` option), 
to allow the solver to be run from elsewhere; e.g., from the repository root:

```
$ java -cp bin net.mattjw.okelydokuly.Solver examples/sd_ex0.csv
```

### 2. Build Script

`build.sh` is a Bash shell script that automates the manual build process. The
script will prepare the `bin` directory before compilation. This includes erasing
any files in `bin` leftover from a previous build.

To run the build script, open your terminal at the repository root directory and
run:

```
$ build.sh
```


### 3. Build with Ant

A simple configuration file for Ant has been written (see: `build.xml`) as another
way to automate the compilation process. Assuming you have 
[Apache Ant](http://ant.apache.org/) installed, simply change to the repository
root directory (the directory containing `build.xml`) and run:

```
$ ant
```

## More Usage Examples

Core functionality of the application is described in the Introduction section.
Here are a few more features.

Feedback is provided for incorrect arguments:

```
$ java net.mattjw.okelydokuly.Solver
Input Sudoku file not specified.
Run with --help to display usage information.
```

```
$ java net.mattjw.okelydokuly.Solver puzzle1.csv puzzle2.csv puzzle3.csv
Too many arguments.
Run with --help to display usage information.
```

Unsolveable Sudoku are handled as follows:

```
$ java net.mattjw.okelydokuly.Solver ../examples/sd_bad1.csv 
A solution to this Sudoku does not exist.
```

Help is provided for malformed Sudoku files:

```
$ java net.mattjw.okelydokuly.Solver ../examples/sd_bad2.csv 
Insufficient cells. Row 6 only contained 8 cells.
```

```
$ java net.mattjw.okelydokuly.Solver ../examples/sd_bad3.csv 
Insufficient rows. File contained only 8 rows.
```

```
$ java net.mattjw.okelydokuly.Solver ../examples/sd_bad4.csv 
Unexpected extra cells on row 1.
```

```
$ java net.mattjw.okelydokuly.Solver ../examples/sd_bad5.csv 
Unexpected non-blank lines at end of file.
```


## Design
For anyone wishing to examine the codebase or extend it, here is a quick overview
of the project.

### Project Structure

The project is structured around the following subdirectories:

```
├── bin              Compiled class files
├── doc              Class documentation (via Javadoc)
├── examples         A selection of example Sudoku puzzles
└── src              Source code
```

### How the Solver Works

The solver represents Sudoku as a Constraint Satisfaction Problem (CSP) and 
solves it using a backtracking search with forwarding checking and 
constraint propagation. This method is efficient even for computationally 
challenging Sudoku grids.

The [Wikipedia page](http://en.wikipedia.org/wiki/Constraint_satisfaction_problem) 
for CSPs offers a good introduction, including some detail on the algorithms 
that can be used to solve them.
For anyone interested in CSPs in general, there's the excellent [Artificial
Intelligence a Modern Approach](http://aima.cs.berkeley.edu/) by Stuart Russell 
and Peter Norvig. Chapter 6 in the third edition is dedicated to constraint 
satisfaction problems, and includes a discussion on representing Sudoku as
a CSP.


### Class Documentation

Javadoc documenation for the codebase is included in the `doc` directory. To 
rebuild the documentation yourself, try the following:

```
$ javadoc -d ./doc -sourcepath ./src -subpackages net
```

This is also automated with Ant:

```
$ ant doc
```

The package structure is as follows:

```
net
└── mattjw
    └── okelydokuly
        ├── ArgParse
        ├── Grid
        ├── InvalidSudokuFileException
        ├── InvalidSudokuGridException
        ├── Solver
        ├── SudokuIO
        ├── UnsolvedSudokuException
        └── ValueSet
```

`Solver` is the main application. It uses `ArgParse` to parse the command line
options and arguments. 

`Grid` represents a mutable Sudoku grid, and encapsulates a lot of the grunt work 
involved in the CSP solving process. This includes assignment of candidate cell
values, constraint checking, forward checking, and constraint propagation. `ValueSet`
represents the legal candidate values that can be assigned to a blank cell. 
The backtracking search, which relies heavily on the `Grid` class, is
implemented in `Solver`.

`SudokuIO` collects methods related to the input/output of Sudoku grids. In 
particular, it is responsible for reading and writing the 9x9 comma-separated
grid format.

For further detail on these classes, including documentation for their methods
and attributes, please refer to the class documentation.


### Style Conventions

Code written following Google's [Java style guide](http://google-styleguide.googlecode.com/svn/trunk/javaguide.html).

## Author

Written by Matt J Williams. 2014. [mattjw.net](http://www.mattjw.net)

<!--- ---------------------------80chars----------------------------------- --->
