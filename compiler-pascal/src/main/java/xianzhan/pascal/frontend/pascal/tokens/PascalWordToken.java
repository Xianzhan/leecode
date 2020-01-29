package xianzhan.pascal.frontend.pascal.tokens;

import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.frontend.pascal.PascalToken;
import xianzhan.pascal.frontend.pascal.PascalTokenType;

/**
 * <h1>PascalWordToken</h1>
 *
 * <p> Pascal word tokens (identifiers and reserved words).</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 *
 * @author Ronald Mak
 */
public class PascalWordToken extends PascalToken {
    /**
     * Constructor.
     *
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public PascalWordToken(Source source) throws Exception {
        super(source);
    }

    /**
     * Extract a Pascal word token from the source.
     *
     * @throws Exception if an error occurred.
     */
    @Override
    protected void extract() throws Exception {
        StringBuilder textBuffer = new StringBuilder();
        char currentChar = currentChar();

        // Get the word characters (letter or digit).  The scanner has
        // already determined that the first character is a letter.
        while (Character.isLetterOrDigit(currentChar)) {
            textBuffer.append(currentChar);
            // consume character
            currentChar = nextChar();
        }

        text = textBuffer.toString();

        // Is it a reserved word or an identifier?
        type = (PascalTokenType.RESERVED_WORDS.contains(text.toLowerCase()))
                // reserved word
                ? PascalTokenType.valueOf(text.toUpperCase())
                // identifier
                : PascalTokenType.IDENTIFIER;
    }
}
