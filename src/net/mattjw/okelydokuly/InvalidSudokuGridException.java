/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuly;

/**
 * Exception thrown if a Sudoku grid represented by a two-dimensional array
 * is not valid.
 */
public class InvalidSudokuGridException extends RuntimeException {

    /**
     * Construct an `InvalidSudokuGridException` instance with a given
     * message.
     */
    public InvalidSudokuGridException(String msg) {
        super(msg);
    }
}
