package xianzhan.pascal.frontend.pascal.tokens;

import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalToken;
import xianzhan.pascal.frontend.pascal.PascalTokenType;

/**
 * <h1>PascalSpecialSymbolToken</h1>
 *
 * <p> Pascal special symbol tokens.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 *
 * @author Ronald Mak
 */
public class PascalSpecialSymbolToken extends PascalToken {
    /**
     * Constructor.
     *
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public PascalSpecialSymbolToken(Source source) throws Exception {
        super(source);
    }

    /**
     * Extract a Pascal special symbol token from the source.
     *
     * @throws Exception if an error occurred.
     */
    @Override
    protected void extract() throws Exception {
        char currentChar = currentChar();

        text = Character.toString(currentChar);
        type = null;

        switch (currentChar) {

            // Single-character special symbols.
            case '+':
            case '-':
            case '*':
            case '/':
            case ',':
            case ';':
            case '\'':
            case '=':
            case '(':
            case ')':
            case '[':
            case ']':
            case '{':
            case '}':
            case '^': {
                nextChar();  // consume character
                break;
            }

            // : or :=
            case ':': {
                // consume ':';
                currentChar = nextChar();

                if (currentChar == '=') {
                    text += currentChar;
                    // consume '='
                    nextChar();
                }

                break;
            }

            // < or <= or <>
            case '<': {
                // consume '<';
                currentChar = nextChar();

                if (currentChar == '=') {
                    text += currentChar;
                    // consume '='
                    nextChar();
                } else if (currentChar == '>') {
                    text += currentChar;
                    // consume '>'
                    nextChar();
                }

                break;
            }

            // > or >=
            case '>': {
                // consume '>';
                currentChar = nextChar();

                if (currentChar == '=') {
                    text += currentChar;
                    // consume '='
                    nextChar();
                }

                break;
            }

            // . or ..
            case '.': {
                // consume '.';
                currentChar = nextChar();

                if (currentChar == '.') {
                    text += currentChar;
                    // consume '.'
                    nextChar();
                }

                break;
            }

            default: {
                nextChar();  // consume bad character
                type = PascalTokenType.ERROR;
                value = PascalErrorCode.INVALID_CHARACTER;
            }
        }

        // Set the type if it wasn't an error.
        if (type == null) {
            type = PascalTokenType.SPECIAL_SYMBOLS.get(text);
        }
    }
}
