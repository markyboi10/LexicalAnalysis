/*
 *   Copyright (C) 2022 -- 2023  Zachary A. Kissel
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package Interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;


import merrimackutil.cli.LongOption;
import merrimackutil.cli.OptionParser;
import merrimackutil.util.Tuple;

/**
 * This provides a simple front end to a descent parser for a toy
 * language.
 * 
 * @author Zach Kissel
 */
public class Interpreter
{
    private static boolean doHelp = false;
    private static boolean doFile = false;
    private static String fileName = null;

    /**
     * Show the license message to the screen.
     */
    public static void showLicense()
    {
        System.out.println("Copyright (C) 2021 -- 2023 Zachary Kissel");
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY.");
        System.out.println(
                "This is free software, and you are welcome to redistribute it");
        System.out.println("under certain conditions.");
    }

    /**
     * Prints a usage message to the screen and exits.
     */
    public static void usage()
    {
        System.err.println("usage:");
        System.err.println("   mfl [--file <filename>]");
        System.err.println("   mfl --help");
        System.err.println("options:"); 
        System.err.println("--file, -f \t\tInterpret the file.");
        System.err.println("--help, -h \t\tDisplay this message");
        System.exit(1);
    }

    /**
     * Runs the interactive mode version of the interpreter.
     */
    public static void runInteractive()
    {
        String line = "";
        Scanner scan = new Scanner(System.in);
        boolean exit = false;
        Lexer lex = null;
        
        showLicense();
        System.out.println();
        System.out.println("MFL interactive mode. Enter .quit to exit.");
        while (!exit)
        {
            System.out.print("mfl> ");
            line = scan.nextLine();

            // Interpret the line if needed.
            line.trim();
            if (!line.isEmpty() && !line.equals(".quit"))
            {
                // Try to interpret the program.
                lex = new Lexer(line);
                Token tok = lex.nextToken();
                while (tok.getType() != TokenType.EOF)
                {
                    System.out.println(tok);
                    tok = lex.nextToken();
                }
              
            }
            else if (line.equals(".quit"))
                exit = true;
        }
        scan.close();
    }

    /**
     * Interprets a file (non-interactive mode.)
     */
    public static void interpretFile()
    {
       Lexer lex = null;

        // Try to interpret the program.
        try
        {
            lex = new Lexer(new File(fileName));

            Token tok = lex.nextToken();
            while (tok.getType() != TokenType.EOF)
            {
                System.out.println(tok);
                tok = lex.nextToken();
            }
        }
        catch (FileNotFoundException ex)
        {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /**
     * Process the command line arguments.
     * 
     * @param args the array of command line arguments.
     */
    public static void processArgs(String[] args)
    {
        OptionParser parser;

        LongOption[] opts = new LongOption[2];
        opts[0] = new LongOption("help", false, 'h');
        opts[1] = new LongOption("file", true, 'f');

        Tuple<Character, String> currOpt;

        parser = new OptionParser(args);
        parser.setLongOpts(opts);
        parser.setOptString("hf:");

        while (parser.getOptIdx() != args.length)
        {
            currOpt = parser.getLongOpt(false);

            switch (currOpt.getFirst())
            {
            case 'h':
                doHelp = true;
                break;
            case 'f':
                doFile = true;
                fileName = currOpt.getSecond();
                break;
            case '?':
                usage();
                break;
            }
        }
    }

    /**
     * The entry point.
     * 
     * @param args the array of strings that represent the command line
     *             arguments.
     */
    public static void main(String[] args)
    {
        // Determine if we are looking at file or command line.
        if (args.length > 2)
            usage();

        // Determine what the user requested.
        processArgs(args);

        // Verify that that this options are not conflicting.
        if (doFile && doHelp)
            usage();

        // Perform the correct action.
        if (doFile)
            interpretFile();
        else if (doHelp)
            usage();
        else
            runInteractive();
    }
}