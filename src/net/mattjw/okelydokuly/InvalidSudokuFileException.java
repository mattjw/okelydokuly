/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuly;

/**
 * Exception thrown if a Sudoku file has an invalid format.
 */
public class InvalidSudokuFileException extends RuntimeException {

    /**
     * Construct an `InvalidSudokuFileException` instance with a given
     * message.
     *
     * @param msg Error message.
     */
    public InvalidSudokuFileException(String msg) {
        super(msg);
    }
}
