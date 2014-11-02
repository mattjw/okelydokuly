/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuly;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * Input and output of Sudoku grids.
 */
public class SudokuIO
{
    public static final char BLANK_CELL_CHAR = '_';

    /*
     * This method will parse a plain text file containing a 9x9 
     * comma-separated representation of a Sudoku grid as a Grid object.
     *
     * Note that this performs little (if any) error checking of the input ASCII 
     * file's content. It is assumed that the file is a correct representation
     * of the Sudoku grid.
     */
    public static Grid parseSudokuFile( File f ) throws FileNotFoundException
    {
        Scanner in = new Scanner( f );

        int[][] m = new int[9][];
        
        for( int row=0; row < 9; row++ )
        {
            // Validation: ensure next row
            if( !in.hasNext() )
                throw new InvalidSudokuFileException( String.format("Insufficient rows. File contained only %d rows.", row) );

            // Process the row
            String str = in.next();
            int[] mRow = new int[9];

            StringTokenizer tokens = new StringTokenizer(str, ",");
            
            for( int col=0; col < 9; col++ )
            {
                // Validation: ensure next cell
                if( !tokens.hasMoreTokens() )
                    throw new InvalidSudokuFileException( String.format("Insufficient cells. Row %d only contained %d cells.", row+1, col) );

                // Process the cell
                String token = tokens.nextToken();
                token = token.trim();

                if( token.length() == 0 )
                    throw new InvalidSudokuFileException( String.format("No value found for cell at column %d of row %d.", col+1, row+1) );
                else if( token.length() > 1 )
                    throw new InvalidSudokuFileException( String.format("Unexpected cell value '%s' in column %d of row %d.", token, col+1, row+1) );

                char c = token.charAt(0);
                
                if( c == BLANK_CELL_CHAR )
                    mRow[col] = -1;
                else
                {
                    if( !Character.isDigit( c ) )
                        throw new InvalidSudokuFileException( String.format("Cell entry '%s' is not an integer.", c) );
                    int val = Integer.parseInt( new Character(c).toString() );
                    
                    if( (val < 1) || (val > 9) )
                        throw new InvalidSudokuFileException( String.format("A cell entry should either be an integer between 1 and 9 or a '%s' to indicate a blank.", BLANK_CELL_CHAR ) );
                    mRow[col] = val;
                }
            }

            // Validation: no more cells
            if( tokens.hasMoreTokens() )
                throw new InvalidSudokuFileException( String.format("Unexpected extra cells on row %d.", row+1) );
            
            m[row] = mRow;
        }

        // Validation: remaining rows are whitespace only
        while( in.hasNext() ) {
            String ln = in.next().trim();
            if( ln.length() != 0 )
                throw new InvalidSudokuFileException( String.format("Unexpected non-blank lines at end of file.") );
        }

        return new Grid( m );
    }
    
    /*
     * This will write a grid to file in the 9x9 comma separated format.
     */
    public static String writeSudokuFile(Grid grid, File f)
    {
        //int[][] matrix = grid.matrix();
        // to do
        return null;
    }
}




