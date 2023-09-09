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
 * Implements a basic token class.
 *
 * @author Zach Kissel
 */
public class Token
{
    private String val; // The value of the token.
    private TokenType type; // The type of token represented.

    /**
     * This is the default constructor.
     */
    public Token()
    {
        val = "";
        type = TokenType.UNKNOWN;

    }

    /**
     * This is the overloaded constructor it sets the value and the token type.
     *
     * @param type the type of the token.
     * @param val  the value stored in the token.
     */
    public Token(TokenType type, String val)
    {
        this.type = type;
        this.val = val;
    }

    /**
     * Get the current value associated with the token.
     *
     * @return the string representing the value of the token.
     */
    public String getValue()
    {
        return val;
    }

    /**
     * Get the current type associated with the token.
     *
     * @return the type of token.
     */
    public TokenType getType()
    {
        return type;
    }

    /**
     * Set the value associated with the token.
     *
     * @param val the value of the token.
     */
    public void setValue(String val)
    {
        this.val = val;
    }

    /**
     * Sets the type of token.
     *
     * @param type the type of token.
     */
    public void setType(TokenType type)
    {
        this.type = type;
    }

    /**
     * Determines if two tokens are equal.
     * 
     * @return true if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        Token tok = (Token) obj;
        return this.val.equals(tok.val);
    }

    /**
     * Return a String representation of the Token.
     *
     * @return a string representing the token.
     */
    @Override
    public String toString() {
        switch (type) {
            case UNKNOWN:
                return "UNKNOWN(" + val + ")";
            case INT:
                return "INT(" + val + ")";
            case REAL:
                return "REAL(" + val + ")";
            case ADD:
                return "ADD";
            case SUB:
                return "SUB";
            case MULT:
                return "MULT";
            case DIV:
                return "DIV";
            case MOD:
                return "MOD";
            case NOT:
                return "NOT";
            case AND:
                return "AND";
            case OR:
                return "OR";
            case GT:
                return "GT";
            case GTE:
                return "GTE";
            case LT:
                return "LT";
            case LTE:
                return "LTE";
            case EQ:
                return "EQ";
            case NEQ:
                return "NEQ";
            case LPAREN:
                return "LPAREN";
            case RPAREN:
                return "RPAREN";
            case ASSIGN:
                return "ASSIGN";
            case VAL:
                return "VAL";
            case COMMENT:
                return "COMMENT";
            case ID:
                return "ID(" + val + ")";
            case EOF:
                return "EOF";
            case TRUE:
                return "TRUE(" + val + ")";
            case FALSE:
                return "FALSE(" + val + ")";
        }
        return "";
    }
}
