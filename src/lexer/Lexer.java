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
package lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * This file implements a basic lexical analyzer.
 *
 * @author Zach Kissel
 */
public class Lexer {

    private BufferedReader input; // The input to the lexer.
    private char nextChar; // The next character read.
    private boolean skipRead; // Whether or not to skip the next char
    // read.
    private long currentLineNumber; // The current line number being processed.

    private enum CharacterClass {
        LETTER, DIGIT, WHITE_SPACE, OTHER, END
    };

    CharacterClass nextClass; // The character class of the nextChar.

    /**
     * Constructs a new lexical analyzer whose source input is a file.
     *
     * @param file the file to open for lexical analysis.
     * @throws FileNotFoundException if the file can not be opened.
     */
    public Lexer(File file) throws FileNotFoundException {
        input = new BufferedReader(new FileReader(file));
        currentLineNumber = 1;
    }

    /**
     * Constructs a new lexical analyzer whose source is a string.
     *
     * @param input the input to lexically analyze.
     */
    public Lexer(String input) {
        this.input = new BufferedReader(new StringReader(input));
        currentLineNumber = 1;
    }

    /**
     * Gets the next token from the stream.
     *
     * @return the next token.
     */
    public Token nextToken() {
        String value = ""; // The value to be associated with the token.

        getNonBlank();
        switch (nextClass) {
            // The state where we are recognizing identifiers.
            // Regex: [A-Za-Z][0-9a-zA-z]*
            case LETTER:
                value += nextChar;
                getChar();

                // Read the rest of the identifier.
                while (nextClass == CharacterClass.DIGIT
                        || nextClass == CharacterClass.LETTER) {
                    value += nextChar;
                    getChar();
                }
                unread(); // The symbol just read is part of the next token.

                if (value.equalsIgnoreCase("TRUE")) {
                    return new Token(TokenType.TRUE, value);
                } else if (value.equals("false")) {
                    return new Token(TokenType.FALSE, value);
                } else if (value.equals("mod")) {
                    return new Token(TokenType.MOD, value);
                } else if (value.equals("not")) {
                    return new Token(TokenType.NOT, value);
                } else if (value.equals("and")) {
                    return new Token(TokenType.AND, value);
                } else if (value.equals("or")) {
                    return new Token(TokenType.OR, value);
                } else if (value.equals("val")) {
                    return new Token(TokenType.VAL, value);
                }
                return new Token(TokenType.ID, value);

            // The state where we are recognizing digits.
            // Regex: [0-9]+
            case DIGIT:
                value += nextChar;
                getChar();

                while (nextClass == CharacterClass.DIGIT) {
                    value += nextChar;
                    getChar();
                }

                if (nextChar == '.') // Decimal point.
                {
                    value += nextChar;
                    getChar();

                    if (nextClass == CharacterClass.DIGIT) {
                        while (nextClass == CharacterClass.DIGIT) {
                            value += nextChar;
                            getChar();
                        }
                        return new Token(TokenType.REAL, value);
                    } else {
                        unread();
                        return new Token(TokenType.UNKNOWN, value);
                    }
                }

                unread();

                return new Token(TokenType.INT, value);

            // Handles all special character symbols.
            case OTHER:
                return lookup();

            // We reached the end of our input.
            case END:
                return new Token(TokenType.EOF, "");

            // This should never be reached.
            default:
                return new Token(TokenType.UNKNOWN, "");
        }
    }

    /**
     * Get the current line number being processed.
     *
     * @return the current line number being processed.
     */
    public long getLineNumber() {
        return currentLineNumber;
    }

    /**
     * **********
     * Private Methods **********
     */
    /**
     * Processes the {@code nextChar} and returns the resulting token.
     *
     * @return the new token.
     */
    private Token lookup() {

        switch (nextChar) {
            case '+':
                return new Token(TokenType.ADD, "+");
            case '-':
                return new Token(TokenType.SUB, "-");
            case '*':
                return new Token(TokenType.MULT, "*");
            case '/':
                return new Token(TokenType.DIV, "/");
            // Handle > and >=
            case '>':
                getChar();
                if (nextChar == '=') {
                    getChar();
                    return new Token(TokenType.GTE, ">=");
                } else {
                    unread();
                    return new Token(TokenType.GT, ">");
                }
            // Handle < and <=
            case '<':
                getChar();
                if (nextChar == '=') {
                    getChar();
                    return new Token(TokenType.LTE, "<=");
                } else {
                    unread();
                    return new Token(TokenType.LT, "<");
                }
            case '=':
                return new Token(TokenType.EQ, "=");
            // Handle ! and !=
            case '!':
                getChar();
                if (nextChar == '=') {
                    getChar();
                    return new Token(TokenType.NEQ, "!=");
                }
            // Right paran
            case ')':
                getChar();
                return new Token(TokenType.RPAREN, ")");
            // Handle := assign
            case ':':
                getChar();
                if (nextChar == '=') {
                    getChar();
                    return new Token(TokenType.ASSIGN, ":=");
                }
            case '(':
                getChar();
                // Start of a comment
                if (nextChar == '*') {
                    getChar();
                    boolean inComment = true;
                    while (inComment) {
                        // While not any * or end detected, consume chars
                        while (nextChar != '*' && nextClass != CharacterClass.END) {
                            getChar();
                        }
                        // IF * detected consume and check for )
                        if (nextChar == '*') {
                            getChar(); 
                            // If ) found consume and return end of comment detected
                            if (nextChar == ')') {
                                inComment = false;
                                getChar();
                                return new Token(TokenType.COMMENT, "COMMENT");
                            }
                        } else {
                            // Handle a possible incomplete comment
                            return new Token(TokenType.UNKNOWN, "POSSIBLE INCOMPLETE COMMENT");
                        }
                    }
                } else {
                    unread(); // Makes sure neigbhoring chars of a left paran aren't ignored
                    return new Token(TokenType.LPAREN, "(");
                }
            // Handle REAL where a 0 is ommitted from the beginning
            case '.':
                getChar();
                if (nextClass == CharacterClass.DIGIT) {
                    String value2 = ".";
                    while (nextClass == CharacterClass.DIGIT) {
                        value2 += nextChar;
                        getChar();
                    }
                    return new Token(TokenType.REAL, value2);
                    // Leave as unkknown, could be recognized as a period in the future
                } else {
                    unread();
                    return new Token(TokenType.UNKNOWN, ".");
                }
            default:
                return new Token(TokenType.UNKNOWN, String.valueOf(nextChar));
        }
    }

    /**
     * Gets the next character from the buffered reader. This updates
     * potentially both {@code nextChar} and {@code nextClass}.
     */
    private void getChar() {
        int c = -1;

        // Handle the unread operation.
        if (skipRead) {
            skipRead = false;
            return;
        }

        try {
            c = input.read();
        } catch (IOException ioe) {
            System.err.println("Internal error (getChar()): " + ioe);
            nextChar = '\0';
            nextClass = CharacterClass.END;
        }

        if (c == -1) // If there is no character to read, we've reached the end.
        {
            nextChar = '\0';
            nextClass = CharacterClass.END;
            return;
        }

        // Set the character and determine it's class.
        nextChar = (char) c;
        if (Character.isLetter(nextChar)) {
            nextClass = CharacterClass.LETTER;
        } else if (Character.isDigit(nextChar)) {
            nextClass = CharacterClass.DIGIT;
        } else if (Character.isWhitespace(nextChar)) {
            nextClass = CharacterClass.WHITE_SPACE;
        } else {
            nextClass = CharacterClass.OTHER;
        }

        // Update the line counter for error checking.
        if (nextChar == '\n') {
            currentLineNumber++;
        }
    }

    /**
     * Gets the next non-blank character. This updates potentially both
     * {@code nextChar} and {@code nextClass}.
     */
    private void getNonBlank() {
        getChar();

        while (nextClass != CharacterClass.END
                && Character.isWhitespace(nextChar)) {
            getChar();
        }
    }

    /**
     * Save the previous character for a future read operation.
     */
    private void unread() {
        skipRead = true;
    }

}
