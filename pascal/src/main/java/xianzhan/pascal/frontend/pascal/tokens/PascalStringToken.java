package xianzhan.pascal.frontend.pascal.tokens;

import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalToken;
import xianzhan.pascal.frontend.pascal.PascalTokenType;

/**
 * <h1>PascalStringToken</h1>
 *
 * <p> Pascal string tokens.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 *
 * @author Ronald Mak
 */
public class PascalStringToken extends PascalToken {
    /**
     * Constructor.
     *
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public PascalStringToken(Source source) throws Exception {
        super(source);
    }

    /**
     * Extract a Pascal string token from the source.
     *
     * @throws Exception if an error occurred.
     */
    @Override
    protected void extract() throws Exception {
        StringBuilder textBuffer = new StringBuilder();
        StringBuilder valueBuffer = new StringBuilder();

        // consume initial quote
        char currentChar = nextChar();
        textBuffer.append('\'');

        // Get string characters.
        do {
            // Replace any whitespace character with a blank.
            if (Character.isWhitespace(currentChar)) {
                currentChar = ' ';
            }

            if ((currentChar != '\'') && (currentChar != Source.EOF)) {
                textBuffer.append(currentChar);
                valueBuffer.append(currentChar);
                // consume character
                currentChar = nextChar();
            }

            // Quote?  Each pair of adjacent quotes represents a single-quote.
            if (currentChar == '\'') {
                while ((currentChar == '\'') && (peekChar() == '\'')) {
                    textBuffer.append("''");
                    // append single-quote
                    valueBuffer.append(currentChar);
                    // consume pair of quotes
                    currentChar = nextChar();
                    currentChar = nextChar();
                }
            }
        } while ((currentChar != '\'') && (currentChar != Source.EOF));

        if (currentChar == '\'') {
            // consume final quote
            nextChar();
            textBuffer.append('\'');

            type = PascalTokenType.STRING;
            value = valueBuffer.toString();
        } else {
            type = PascalTokenType.ERROR;
            value = PascalErrorCode.UNEXPECTED_EOF;
        }

        text = textBuffer.toString();
    }
}
