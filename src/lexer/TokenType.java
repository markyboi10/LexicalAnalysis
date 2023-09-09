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

/**
 * An enumeration of token types.
 */
public enum TokenType
{
    /**
     * An integer token.
     */
    INT,

    /**
     * A real number token.
     */
    REAL,

    /**
     * An identifier token.
     */
    ID,
    
    /**
     * A true boolean token, reserved word
     */
    TRUE,
    
    /**
     * A false boolean token, reserved word
     */
    FALSE,

    /**
     * Add operation token.
     */
    ADD,
    
    /**
     * Sub operation token
     */
    SUB,
    
    /**
     * Multiplication operation token
     */
    MULT,
    
    /**
     * Division operation token
     */
    DIV,
    
    /**
     * Mod operation token, reserved word
     */
    MOD,
    
    /**
     * Not operation token, reserved word
     */
    NOT,
    
    /**
     * Add operation token, reserved word
     */
    AND,
    
    /**
     * Or operarion token, reserved word
     */
    OR,
    
    /**
     * < operation token
     */
    GT,
    
    /**
     * <= operation token
     */
    GTE,
    
    /**
     * > operation token
     */
    LT,
    
    /**
     * >= operation token
     */
    LTE,
    
    /**
     * Equals operation token
     */
    EQ,
    
    /**
     * != not equal operation token
     */
    NEQ,
    
    /**
     * Left parentheses operation token
     */
    LPAREN,
    
    /**
     * Right parentheses operation token
     */
    RPAREN,
    
    /**
     * Assignment operation token, :=
     */
    ASSIGN,
    
    /**
     * Value operation token, reserved word
     */
    VAL,
    
    /**
     * Comment block operation token
     */
    COMMENT,
    
    /**
     * An unknown token.
     */
    UNKNOWN,

    /**
     * The end of the file token.
     */
    EOF
}
