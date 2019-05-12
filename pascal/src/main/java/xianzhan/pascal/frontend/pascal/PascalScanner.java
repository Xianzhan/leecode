package xianzhan.pascal.frontend.pascal;

import xianzhan.pascal.frontend.EofToken;
import xianzhan.pascal.frontend.Scanner;
import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.tokens.PascalErrorToken;
import xianzhan.pascal.frontend.pascal.tokens.PascalNumberToken;
import xianzhan.pascal.frontend.pascal.tokens.PascalSpecialSymbolToken;
import xianzhan.pascal.frontend.pascal.tokens.PascalStringToken;
import xianzhan.pascal.frontend.pascal.tokens.PascalWordToken;

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
        skipWhiteSpace();

        Token token;
        char currentChar = currentChar();

        // Construct the next token.  The current character determines the
        // token type.
        if (currentChar == Source.EOF) {
            token = new EofToken(source);
        } else if (Character.isLetter(currentChar)) {
            token = new PascalWordToken(source);
        } else if (Character.isDigit(currentChar)) {
            token = new PascalNumberToken(source);
        } else if (currentChar == '\'') {
            token = new PascalStringToken(source);
        } else if (PascalTokenType.SPECIAL_SYMBOLS.containsKey(Character.toString(currentChar))) {
            token = new PascalSpecialSymbolToken(source);
        } else {
            token = new PascalErrorToken(
                    source,
                    PascalErrorCode.INVALID_CHARACTER,
                    Character.toString(currentChar)
            );
            // consume character
            nextChar();
        }

        return token;
    }

    /**
     * Skip whitespace characters by consuming them.  A comment is whitespace.
     *
     * @throws Exception if an error occurred.
     */
    private void skipWhiteSpace() throws Exception {
        char currentChar = currentChar();

        while (Character.isWhitespace(currentChar) || (currentChar == '{')) {

            // Start of a comment?
            if (currentChar == '{') {
                do {
                    // consume comment characters
                    currentChar = nextChar();
                } while ((currentChar != '}') && (currentChar != Source.EOF));

                // Found closing '}'?
                if (currentChar == '}') {
                    // consume the '}'
                    currentChar = nextChar();
                }
            }

            // Not a comment.
            else {
                // consume whitespace character
                currentChar = nextChar();
            }
        }
    }
}
