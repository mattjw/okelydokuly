/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuly;

/* 
 * Exception thrown if a Sudoku puzzle could not be solved by the solver; e.g.,
 * because the grid arrangement is invalid.
 */
public class UnsolvedSudokuException extends RuntimeException {

    /*
     * Construct an `UnsolvedSudokuException` instance with a given
     * message.
     */
    public UnsolvedSudokuException(String msg) {
        super(msg);
    }
}
