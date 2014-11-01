/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuley;

import java.util.*;
import java.io.*;

/*
 * This class has two purposes:
 *  1. It implements a backtracking search algorithm which solves the Sudoku
 *     puzzle as a constraint satisfaction problem.
 *  2. It is also an application which acts as the front-end of the Sudoku solver.
 *     This simply takes in the filename of a Sudoku puzzle (represented in an ASCII 
 *     format) as a command line argument and attempts to read in and solve it.
 */
public class Solver
{
    /*
     * This method implements a backtracking search with forward checking algorithm
     * to solve the inputted Sudoku grid.
     * This is based on the backtracking algorithm as described in Artificial
     * Intelligence: A Modern Approach (Russel, S. and Norvig, P. pp. 142).
     * null is return to indicate failure to solve a puzzle.
     */
    public static Grid backtrackingSearch( Grid grid )
    {
        if( grid.isComplete() )
            return grid;
        
        // Locate an unassigned cell for assignment
        int[] loc = grid.selectUnassignedCell();
        int row = loc[0];
        int col = loc[1];
        
        // Get the (ordered) values for this unassigned cell (held in an 
        // ordered list). The values in this list are only those which will 
        // not violate any constraints if assigned to the given cell.
        List<Integer> values = grid.getOrderedDomainValues( row, col );
        
        // Try assignment of the cell with each value in turn
        for( Integer valObj : values )
        {
            int val = valObj.intValue();
            grid.assignCell( row, col, val );
            
            // Only recurse if this assignment has resulted in an unassigned 
            // cell's domain having no legal values (part of forward checking)
            if( grid.allDependentsHaveLegalDomains( row, col ) )
            {
                Grid result = backtrackingSearch( grid );
                if( result != null )
                    return result;
            }
            
            grid.unassignCell( row, col );
        }
        
        return null;
    }
    
    
    
    
    public static void main( String[] args )
    {
        /* Brief input checking */
        if( args.length < 1 )
        {
            System.out.println( "Missing argument - expected the filename of a Sudoku puzzle" );
            System.exit( 0 );
        }
        else if( args.length > 1 )
        {
            System.out.println( "Too many arguments - only one argument (the filename of a Sudoku puzzle) should be given" );
            System.exit( 0 );
        }
        
        /* Solve the Sudoku puzzle */
        try
        {
            File f = new File( args[0] );
            Grid g = Grid.parseGrid( f );
            
            Grid result = backtrackingSearch( g );
            
            if( result == null )
                System.out.println( "A solution to this Sudoku does not exist!" );
            else
                System.out.println( result.toOutputFormat() );
        }
        catch( FileNotFoundException ex )
        {
            System.out.println( ex );
        }
    }
}
