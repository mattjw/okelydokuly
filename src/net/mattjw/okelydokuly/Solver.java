/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuly;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.mattjw.okelydokuly.ArgParse.Arguments;

/**
 * Solve Sudoku puzzles using a constraint-satisfcation backtracking search.
 *
 * This class has two purposes:
 * <ul>
 *  <li> It implements a backtracking search algorithm which solves the Sudoku
 *       puzzle as a constraint satisfaction problem, relying heavily on the 
 *       features implemented in `Grid`. </li>
 *  <li> It is the application that acts as the front-end of the Sudoku solver.
 *       </li>
 *  </ul>
 */
public class Solver
{
    private static final String SUGGEST_HELP = "Run with --help to display usage information.";

    /**
     * This method implements a backtracking search with forward checking 
     * algorithm to solve the inputted Sudoku grid.
     * This is based on the backtracking algorithm as described in Artificial
     * Intelligence: A Modern Approach (Russell and Norvig).
     *
     * null is returned to indicate failure to solve a puzzle.
     * null is also used during recusrive backtracking to indicate a particular
     * branch of the search space is unsolveable.
     *
     * This method modifies the Grid argument during the solving procedure.
     *
     * @param grid The Sudoku grid being solved.
     * @return A solution to the Sudoku.
     */
    public static Grid backtrackingSearch(Grid grid) {
        if(grid.isComplete())
            return grid;
        
        // Locate an unassigned cell for assignment
        int[] loc = grid.selectUnassignedCell();
        int row = loc[0];
        int col = loc[1];
        
        // Get the (ordered) values for this unassigned cell (held in an 
        // ordered list). The values in this list are only those which will 
        // not violate any constraints if assigned to the given cell.
        List<Integer> values = grid.getOrderedDomainValues(row, col);
        
        // Try assignment of the cell with each value in turn
        for(Integer valObj : values) {
            int val = valObj.intValue();
            grid.assignCell(row, col, val);
            
            // Only recurse if this assignment has resulted in an unassigned 
            // cell's domain having no legal values (part of forward checking)
            if(grid.allDependantsHaveLegalDomains(row, col)) {
                Grid result = backtrackingSearch(grid);
                if(result != null)
                    return result;
            }
            
            grid.unassignCell(row, col);
        }
        
        return null;
    }

    private static final int RETCODE_OK = 0;
    private static final int RETCODE_ARGPARSE = 1;
    private static final int RETCODE_FILEIO = 2;
    private static final int RETCODE_UNSOLVED = 3;
    
    /**
     * Main point of entry for the Sudoku solver.
     * @param rawArgs Command line arguments.
     */
    public static void main(String[] rawArgs) {
        //
        // Parse arguments
        Arguments args = null;
        try {
            args = ArgParse.parse(rawArgs);
        }
        catch(IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            System.out.println(SUGGEST_HELP);
            System.exit(RETCODE_ARGPARSE);
        }
            
        if(args.helpFlag) {
            System.out.println(ArgParse.USAGE);
            System.exit(RETCODE_OK);
        }

        //
        // Validate input and output 
        File fIn = args.sudokuIn;
        File fOut = args.sudokuOut;

        if(!fIn.exists()) {
            System.out.println("Could not find input file: " + fIn + ".");
            System.exit(RETCODE_FILEIO);
        }

        if(!fIn.canRead()) {
            System.out.println("Cannot read input file: " + fIn + ".");
            System.exit(RETCODE_FILEIO);
        }

        if(fOut != null) {
            if(fOut.exists() && !fOut.canWrite()) {
                System.out.println("Cannot write to existing output file: " + fOut + ".");
                System.exit(RETCODE_FILEIO);
            }
        }
        
        //
        // Read sudoku
        Grid g = null;
        try {
            g = SudokuIO.parseSudokuFile(fIn);
        }
        catch(FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(RETCODE_FILEIO);
        }
        catch(InvalidSudokuFileException ex) {
            System.out.println(ex.getMessage());
            System.exit(RETCODE_FILEIO);
        }
        catch(InvalidSudokuGridException ex) {
            System.out.println(ex.getMessage());
            System.exit(RETCODE_FILEIO);
        }

        //
        // Solve the puzzle
        Grid result = backtrackingSearch(g);
        
        if(result == null) {
            System.out.println("A solution to this Sudoku does not exist.");
            System.exit(RETCODE_UNSOLVED);
        }

        //
        // Output -- file or command line, as requested
        if(fOut == null) {
            System.out.println(result);
            System.exit(RETCODE_OK);
        }
        else {
            try {
                SudokuIO.writeSudokuFile(result, fOut);
                System.exit(RETCODE_OK);
            }
            catch(FileNotFoundException ex) {
                System.out.println("Output file error: " + ex.getMessage());
                System.exit(RETCODE_FILEIO);
            }
            catch(IOException ex) {
                System.out.println(ex.getMessage());
                System.exit(RETCODE_FILEIO);
            }
        }
    }
}


