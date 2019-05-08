package xianzhan.pascal.frontend.pascal;

import xianzhan.pascal.frontend.EofToken;
import xianzhan.pascal.frontend.Scanner;
import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.frontend.Token;

/**
 * <h1>PascalScanner</h1>
 *
 * <p>The Pascal scanner.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 *
 * @author Ronald Mak
 */
public class PascalScanner extends Scanner {
    /**
     * Constructor
     *
     * @param source the source to be used with this scanner.
     */
    public PascalScanner(Source source) {
        super(source);
    }

    /**
     * Extract and return the next Pascal token from the source.
     *
     * @return the next token.
     * @throws Exception if an error occurred.
     */
    @Override
    protected Token extractToken() throws Exception {
        Token token;
        char currentChar = currentChar();

        // Construct the next token.  The current character determines the
        // token type.
        if (currentChar == Source.EOF) {
            token = new EofToken(source);
        } else {
            token = new Token(source);
        }

        return token;
    }
}
