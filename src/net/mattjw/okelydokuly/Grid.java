/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuly;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.Scanner;
import java.util.List;
import java.util.Collections;

/*
 * This class represents a Sudoku grid throughout the solving process; i.e.,
 * from unsolved, to partially solved, to solved. It implements many of the CSP
 * operations (such as assignment) that relate to the Sudoku grid.
 * Also, changes to a Sudoku grid as part of the solving process (e.g. due to
 * assignment) are managed and represented by this class.
 *
 * Note that this is coded specifically to 9x9 Sudoku grids.
 */
public class Grid
{
    private int[][] matrix;    // Internal representation of a Sudoku grid
    
    private ValueSet[][] aVals;   // Each entry in this 2D array is a set 
                                  // of all the possible values the 
                                  // corresponding Sudoku cell may be 
                                  // assigned with (note that this will 
                                  // only be done for the cells that
                                  // are not assigned in the initial
                                  // Sudoku grid; other cells will be
                                  // left null).
                                  // This data structure is to support
                                  // the forward checking process.
    
    private int numAssigned;     // Tracks the number of assigned cells in the
                                 // grid (this allows for more efficient
                                 // checking of whether the grid is complete)
    
    /*
     * Construct a Sudoku grid object from a 2 dimensional array of integers.
     * -1 is used to indicate an empty cell in the Sudoku.
     *
     * Note that this will not check to see if the 2 dimensional array 
     * representation of the Sudoku is valid.
     * It is assumed that the input array is:
     *  - of the correct size
     *  - a valid Sudoku grid (i.e. meets the constraints of Sudoku grids)
     */
    public Grid( int[][] matrix )
    {
        assert matrix.length == 9;
        assert matrix[0].length == 9; 
        
        
        this.matrix = matrix;
        
        
        /* Set up the potential values matrix (the 2D array named aVals)
           and count the number of unassigned variables (for the purpose
           of initialising numAssigned)... */
        // Initialise matrix (for potential values) and the count
        aVals = new ValueSet[9][9];
        numAssigned = 0;
        
        // Determine the possible values each entry may be assigned with
        for( int row=0; row < 9; row++ )
        {
            for( int col=0; col < 9; col++ )
            {
                // Check if the cell is a blank (unassigned) cell
                if( matrix[row][col] == -1 )
                    aVals[row][col] = getPotentialValues( matrix, row, col );
                else
                    numAssigned++;
            }
        }
    }

    /* 
     * Returns a string representation of the Sudoku grid.
     * This follows the 9 row by 9 column comma-separated format, with the
     * exception that blank cells (if present) are represented by underscores.
     */
    public String toString()
    {
        StringBuffer buff = new StringBuffer();
        
        for( int row=0; row < matrix.length; row++ )
        {
            for( int col=0; col < matrix[row].length; col++ )
            {
                if( matrix[row][col] == -1 )
                    buff.append( "_" );
                else
                    buff.append( matrix[row][col] );
                if( col < (matrix[row].length-1) )
                    buff.append(",");
            }
            
            // Only insert newlines BETWEEN rows
            if( row < (matrix.length-1) )
                buff.append( "\n" );
        }
        
        return buff.toString();
    }
    
    /*
     * ********** CSP SOLVER METHODS **********
     */
    
    /*
     * This method will choose an unassigned cell in the Sudoku grid.
     * It returns a two element array. The first element is the row number of 
     * the located cell, the second element is the column number of the located 
     * cell.
     *
     * The process of deciding the cell is based on the 'most constrained 
     * variable' heuristic. This means that the chosen cell is the one which has
     * the fewest potential values (i.e. the fewest number of values in the 
     * ValueSet (i.e. the domain for the cell)).
     *
     * It is assumed that the gird is not complete when this method is called.
     */
    public int[] selectUnassignedCell()
    {
        assert !isComplete();
        
        
        int bestRow = -1;
        int bestCol = -1;
        int bestNumChoices = 10;     // Tracks the best number of potential 
                                     // values for an unnassigned cell 
                                     // (1 is the best)
        
        /* Iterate over the whole matrix to find unassigned cells */
        for( int row=0; row < 9; row++ )
        {
            for( int col=0; col < 9; col++ )
            {
                // Check if we have an unassigned cell
                if( matrix[row][col] == -1 )
                {
                    int numChoices = aVals[row][col].size();
                    if( numChoices < bestNumChoices )
                    {
                        bestRow = row;
                        bestCol = col;
                        bestNumChoices = numChoices;
                        
                        // 1 is the best possible number of choices for a cell,
                        // so we can return early if we find such a cell
                        if( bestNumChoices == 1 )
                            return new int[] { bestRow, bestCol };
                    }
                }
            }
        }
        
        return new int[] { bestRow, bestCol };
    }
    
    /*
     * This method will return a list of the possible values that the given cell
     * may be assigned with.
     *
     * The order of the values in the returned list is such that the first value
     * is the least-constraining-value and the last is the most-constraining-value
     * (hence this method implements the least-constraining-value heuristic). The
     * least-constrained value for an unassigned cell X is the value v that rules
     * out fewest choices for other unassigned cells that are affected by the
     * assignment of v to X.
     */
    public List<Integer> getOrderedDomainValues( int row, int col )
    {
        assert (row >= 0) && ( row <= 8 );
        assert (col >= 0) && ( col <= 8 );
        assert matrix[row][col] == -1;
        
        
        // We need to construct a list of values that is sorted in order of how
        // constrained the value is. To do this, two lists are used;
        // one holds the actual values, the other holds the corresponding
        // number of choices ruled out by choosing that value (storing these
        // values means they do not have to be recomputed at each insertion).
        List<Integer> valList = new Vector<Integer>();
        List<Integer> cntList = new Vector<Integer>();
        
        for( Integer val : aVals[row][col] )
        {
            int countInt = countChoicesRuledOut( row, col, val.intValue() ); 
            Integer countObj = new Integer( countInt );
            
            int index = Collections.binarySearch( cntList, countObj );
            
            if( index < 0 )
            {
                int insertionPoint = -(index) - 1;
                
                valList.add( insertionPoint, val );
                cntList.add( insertionPoint, countObj );
            }
            else
            {
                int insertionPoint = index + 1;
                valList.add( insertionPoint, val );
                cntList.add( insertionPoint, countObj );
            }
        }
        
        return valList; 
    }
    
    /*
     * This method will assign an unassigned cell will the given value.
     *
     * After carrying this out, the method also handles updating the ValueSets
     * (domains) for the cells affected by this assignment. This is necessary to
     * support forward checking in the search algorithm. Specifically, this update 
     * process looks at each unassigned variable Y that is affected by this
     * assignment and removes from Y's domain (i.e. Y's ValueSet) any value
     * that becomes inconsistent due to this assignment.
     *
     * Finally, this method will also update the assignedCount (this is important
     * for the isComplete() method).
     */
    public void assignCell( int row, int col, int val )
    {
        assert (row >= 0) && ( row <= 8 );
        assert (col >= 0) && ( col <= 8 );
        assert (val >= 1) && ( val <= 9 );
        assert matrix[row][col] == -1 : "Only an unassigned cell may be assigned a value";
        assert aVals[row][col].contains( val ) : "A cell may only be assigned with a value that is in its set of potential values" ;
        
        
        /* Carry out the assignment */
        matrix[row][col] = val;
        numAssigned++;
        
        
        /* Update the potential values for this cell's row, column and square */
        // Update row...
        for( int i=0; i < 9; i++ )
        {
            // Check the cell is unassigned
            if( matrix[row][i] == -1 )
                aVals[row][i].remove( val );
        }
        
        // Update column...
        for( int i=0; i < 9; i++ )
        {
            // Check the cell is unassigned
            if( matrix[i][col] == -1 )
                aVals[i][col].remove( val );
        }
        
        // Update square...
        // Calculate the base (top-left) indexes for this cell's square:
        int baseRow = row - (row%3);
        int baseCol = col - (col%3);
        
        for( int i=0; i < 9; i++ )
        {
            int rowOff = i / 3;        
            int colOff = i % 3;
            
            // Check the cell is unassigned
            if( matrix[baseRow+rowOff][baseCol+colOff] == -1 )
                aVals[baseRow+rowOff][baseCol+colOff].remove( val );
        }
    }
    
    /*
     * This method will unassign an assigned cell.
     * After carrying this out, the method also handles updating the ValueSets
     * (domains) for the cells affected by this unassignment. The aim of the
     * update is to return the domains of all the unassigned cells back to what 
     * they were before the assignment.
     * This is important since when the search backtracks to try a different
     * assignment, we need to return the grid to the state it was before the
     * assignment was made.
     *
     * It assumed that the cell this method works on is one that has been 
     * previously assigned by the assignCell() method. In other words, the 
     * method should NOT be used to unassign a value that was given in the 
     * initial Sudoku grid.
     *
     * Finally, this method will also update the assignedCount (this is 
     * important for the isComplete() method).
     */
    public void unassignCell( int row, int col )
    {
        assert (row >= 0) && ( row <= 8 );
        assert (col >= 0) && ( col <= 8 );
        assert matrix[row][col] != -1 : "Only non-blank cells can be unassigned";
        assert aVals[row][col] != null : "Only cells that were unassigned in the initial grid may be unassigned";
        
        
        /* Carry out unassignment */
        int val = matrix[row][col];
        matrix[row][col] = -1;
        numAssigned--;
        
        
        /* Update the potential values for this cell's row, column and square */
        // Update row...
        for( int i=0; i < 9; i++ )
        {
            // Check the cell was INITIALLY unassigned
            // (a null entry in the aVals array indicates that the corresponding
            // Sudoku cell is not assignable, thus does not need to have
            // its domain recalculated)
            if( aVals[row][i] != null )
                aVals[row][i] = getPotentialValues( matrix, row, i );
        }
        
        // Update column...
        for( int i=0; i < 9; i++ )
        {
            // Check the cell was INITIALLY unassigned
            if( aVals[i][col] != null )
                aVals[i][col] = getPotentialValues( matrix, i, col );
        }
        
        // Update square...
        // Calculate the base (top-left) indexes for this cell's square:
        int baseRow = row - (row%3);
        int baseCol = col - (col%3);
        
        for( int i=0; i < 9; i++ )
        {
            int rowOff = i / 3;        
            int colOff = i % 3;
            
            // Check the cell was INITIALLY unassigned
            if( aVals[baseRow+rowOff][baseCol+colOff] != null )
                aVals[baseRow+rowOff][baseCol+colOff] = getPotentialValues( matrix, baseRow+rowOff, baseCol+colOff );
        }
    }
    
    /*
     * This method will check if this Grid's assignment is complete. Assignment
     * in a CSP is defined to be complete if every variable is mentioned.
     * In the case of a Sudoku grid, the grid is complete if every cell has a
     * value.
     * Note that this method only checks for completeness of assignment -- it is
     * not concerned with any consistency checking.
     */
    public boolean isComplete()
    {
        return numAssigned == 81;
    }
    
    /*
     * This method will look at each unassigned cell that is affected by
     * assignment to the given cell. (These are the unassigned cells on the
     * same row, column, and/or square as the given cell).
     * For each of these cells, the method will check to see if there is at
     * least one legal value in the cell's domain.
     *
     * If any of these domains are empty, then the current assignments to this
     * Sudoku grid are such that it cannot possibly lead to a solution.
     * (false is returned if any of the domains are empty, true otherwise)
     * This is part of the forward checking process.
     */
    public boolean allDependentsHaveLegalDomains( int row, int col )
    {
        assert (row >= 0) && ( row <= 8 );
        assert (col >= 0) && ( col <= 8 );
        
        
        /* Check the domains for cells in the same row */ 
        for( int i=0; i < 9; i++ )
        {
            // Check if cell is unassigned
            if( matrix[row][i] == -1 )
            {
                if( aVals[row][i].isEmpty() )
                    return false;
            }
        }
        
        /* Check the domains for cells in the same column */ 
        for( int i=0; i < 9; i++ )
        {
            // Check if cell is unassigned
            if( matrix[i][col] == -1 )
            {
                if( aVals[i][col].isEmpty() )
                    return false;
            }
        }
        
        /* Check the domains for cells in the same square */
        // Calculate the base (top-left) indexes for this cell's square  
        int baseRow = row - (row%3);
        int baseCol = col - (col%3);
        
        for( int i=0; i < 9; i++ )
        {
            int rowOff = i / 3;        
            int colOff = i % 3;
            
            // Check if cell is unassigned
            if( matrix[baseRow+rowOff][baseCol+colOff] == -1 )
            {
                if( aVals[baseRow+rowOff][baseCol+colOff].isEmpty() )
                    return false;
            }
        }
        
        return true;
    }
    
    /*
     * This method will count the number of times the given value
     * appears in the domains of the unassigned cells that are
     * dependent on the given cell.
     * This value is equivalent to the number of choices in other 
     * unassigned cells that get ruled out due to the assignment of 
     * the given value (this is necessary for the least-constraining-value 
     * heuristic).
     *
     * The method is careful to ensure that the same cell is not
     * counted twice. It also makes sure that the domain of the
     * given cell (the cell given in the method input) is NOT
     * counted either.
     */
    private int countChoicesRuledOut( int row, int col, int val )
    {
        assert (row >= 0) && ( row <= 8 );
        assert (col >= 0) && ( col <= 8 );
        
        
        int count = 0;
        
        /* Pre-calculations... */
        // Find the top-left (TL) indexes for this cell's square:
        int sqTLRow = row - (row%3);
        int sqTLCol = col - (col%3);
        
        // Find the bottom-right (BR) indexes for this cell's square:
        int sqBRRow = sqTLRow + 2;
        int sqBRCol = sqTLCol + 2;
        
        
        /* Iteration over cells... */
        // Iterate over the cells in the row that precede the square
        for( int i=0; i < sqTLCol; i++ )
        {
            if( matrix[row][i] == -1 )
            {
                if( aVals[row][i].contains( val ) )
                    count++;
            }
        }

        // Iterate over the cells in the row that succeed the square
        for( int i=sqBRCol+1; i < 9; i++ )
        {
            if( matrix[row][i] == -1 )
            {
                if( aVals[row][i].contains( val ) )
                    count++;
            }
        }
        
        // Iterate over the cells in the column that precede the square
        for( int i=0; i < sqTLRow; i++ )
        {
            if( matrix[i][col] == -1 )
            {
                if( aVals[i][col].contains( val ) )
                    count++;
            }
        }
        
        // Iterate over the cells in the column that succeed the square
        for( int i=sqBRRow+1; i < 9; i++ )
        {
            if( matrix[i][col] == -1 )
            {
                if( aVals[i][col].contains( val ) )
                    count++;
            }
        }
        
        // Iterate over all the cells in the square (except for the same cell)
        for( int i=0; i < 9; i++ )
        {
            int rowOffset = i / 3;        
            int colOffset = i % 3;
            
            int sqRow = sqTLRow + rowOffset;
            int sqCol = sqTLCol + colOffset;
            
            // Check that the cell in the square isn't the one given by the 
            // method
            if( !( (sqRow == row) && (sqCol == col) ) )
            {
                if( matrix[sqRow][sqCol] == -1 )
                {
                    if( aVals[sqRow][sqCol].contains( val ) )
                        count++;
                }
            }
        }
        
        return count;
    }
    
    /*
     * This method will create a ValueSet containing each value that can be
     * legally assigned to the given cell in the given matrix (2D array)
     * representation of a Sudoku.
     */
    private static ValueSet getPotentialValues( int[][] matrix, int row, int col )
    {
        assert (row >= 0) && ( row <= 8 );
        assert (col >= 0) && ( col <= 8 );
        assert matrix.length == 9;
        assert matrix[0].length == 9;
        
        
        int[] count = new int[9];
        
        
        /* Count existing values in the row */ 
        for( int i=0; i < 9; i++ )
        {
            int val = matrix[row][i];
            if( val != -1 )
            {
                count[val-1]++;
            }
        }
        
        /* Count existing values in the column */ 
        for( int i=0; i < 9; i++ )
        {
            int val = matrix[i][col];
            if( val != -1 )
            {
                count[val-1]++;
            }
        }
        
        /* Count existing values in the square */
        // Calculate the base (top-left) indexes for this cell's square  
        int baseRow = row - (row%3);
        int baseCol = col - (col%3);
        
        for( int i=0; i < 9; i++ )
        {
            int rowOff = i / 3;        
            int colOff = i % 3;
            
            int val = matrix[baseRow+rowOff][baseCol+colOff];
            if( val != -1 )
            {
                count[val-1]++;
            }
        }
        
        /* Finally, determine the potential values */
        ValueSet set = new ValueSet();
        
        for( int i=0; i < 9; i++ )
        {
            if( count[i] == 0 )
                set.add( new Integer(i+1) );
        }
        
        return set;
    }
}




