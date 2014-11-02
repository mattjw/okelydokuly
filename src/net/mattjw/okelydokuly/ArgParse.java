/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuly;

import java.io.File;

/**
 * Parse arguments for the Sudoku command line tool.
 */
public class ArgParse {
    public static final String NAME = "okelydokuly";
    public static final String APP_CLASSNAME = "Solver";
    public static final String USAGE = 
        NAME + ": a Sudoku puzzle solver\n" + 
        "\n" +
        "Usage: \n" +
        "  " + APP_CLASSNAME + " [--help] infile [outfile]\n" +
        "\n" +
        "  Solves the Sudoku in infile and saves the solution to outfile. If the output\n" +
        "  file is ommitted, the solution is printed to the command line.\n" +
        "\n" +
        "  Sudoku files are read and written in plain text 9x9 comma-separated values\n" +
        "  format.\n" +
        "\n" +
        "Options:\n" + 
        "  --help    Display this help prompt.\n";

    private static final String HELP_FLAG = "--help";

    /**
     * Represents the arguments for the command line tool, and information on 
     * the attempt to parse the arguments.
     */
    public static class Arguments {
        public final File sudokuIn;
        public final File sudokuOut;
        public final boolean helpFlag;

        /**
         * Arguments for the tool.
         * @param inFile Location of the Sudoku to be solved.
         * @param outFile Location to save the solution (may be null if file output not requested).
         * @param helpFlag The flag to print help information. If true, then other arguments may be null.
         */
        public Arguments(File sudokuIn, File sudokuOut, boolean helpFlag) {
            this.sudokuIn = sudokuIn;
            this.sudokuOut = sudokuOut;
            this.helpFlag = helpFlag;
        }
    }

    /**
     * Process input arguments.
     *
     * File paths are not validated by this method.
     *
     * @return The parsed command line arguments.
     * @exception IllegalArgumentException For invalid arguments.
     */
    public static Arguments parse(String[] args) {
        if(args.length == 0)
            throw new IllegalArgumentException("Input Sudoku file not specified.");

        if(args.length > 2)
            throw new IllegalArgumentException("Too many arguments.");

        // Handle the presence of the help flag
        if(HELP_FLAG.equals(args[0]))
            return new Arguments(null, null, true);

        // No help flag -- expect paths to sudoku files
        String inPath = args[0];
        File inSudoku = new File(inPath);

        String outPath = null;
        File outSudoku = null;

        if(args.length == 2) {
            outPath = args[1];
            outSudoku = new File(outPath);
        }

        Arguments parsedArgs = new Arguments(inSudoku, outSudoku, false);
        return parsedArgs;
    }
}





