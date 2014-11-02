/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuly;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * Input and output of Sudoku grids.
 */
public class SudokuIO
{
    /*
     * This method will parse a file containing an ASCII representation of a 
     * Sudoku grid as a Grid object.
     *
     * Note that this performs little (if any) error checking of the input ASCII 
     * file's content. It is assumed that the file is a correct representation
     * of the Sudoku grid.
     */
    public static Grid parseGrid( File f ) throws FileNotFoundException
    {
        Scanner in = new Scanner( f );

        int[][] m = new int[9][];
        
        for( int row=0; row < 9; row++ )
        {
            String str = in.next();
            int[] mRow = new int[9];
            
            for( int col=0; col < 9; col++ )
            {
                char c = str.charAt( col );
                
                if( c == '_' )
                    mRow[col] = -1;
                else
                {
                    if( !Character.isDigit( c ) )
                        throw new RuntimeException( "Cell entry " + c + " is not an integer" );
                    int val = Integer.parseInt( new Character(c).toString() );
                    
                    if( (val < 1) || (val > 9) )
                        throw new RuntimeException( "A cell entry should either be an integer between 1 and 9 or a _" );
                    mRow[col] = val;
                }
            }
            
            m[row] = mRow;
        }         
        
        
        return new Grid( m );
    }
    
    /*
     * This will write a grid to file in the 9x9 comma separated format.
     */
    public static String writeGrid(Grid grid, File f)
    {
        //int[][] matrix = grid.matrix();
        // to do
        return null;
    }
}




