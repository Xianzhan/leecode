package xianzhan.pascal.frontend.pascal;

import xianzhan.pascal.frontend.Parser;
import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageType;

/**
 * <h1>PascalErrorHandler</h1>
 *
 * <p>Error handler Pascal syntax errors.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 *
 * @author Ronald Mak
 */
public class PascalErrorHandler {
    private static final int MAX_ERRORS = 25;

    /**
     * count of syntax errors
     */
    private static int errorCount = 0;

    /**
     * Getter.
     *
     * @return the syntax error count.
     */
    public int getErrorCount() {
        return errorCount;
    }

    /**
     * Flag an error in the source line.
     *
     * @param token     the bad token.
     * @param errorCode the error code.
     * @param parser    the parser.
     * @return the flagger string.
     */
    public void flag(Token token, PascalErrorCode errorCode, Parser parser) {
        // Notify the parser's listeners.
        Message message = new Message(
                MessageType.SYNTAX_ERROR,
                new Object[]{
                        token.getLineNumber(),
                        token.getPosition(),
                        token.getText(),
                        errorCode.toString()
                }
        );
        parser.sendMessage(message);

        if (++errorCount > MAX_ERRORS) {
            abortTranslation(PascalErrorCode.TOO_MANY_ERRORS, parser);
        }
    }

    /**
     * Abort the translation.
     *
     * @param errorCode the error code.
     * @param parser    the parser.
     */
    public void abortTranslation(PascalErrorCode errorCode, Parser parser) {
        // Notify the parser's listeners and then abort.
        String fatalText = "FATAL ERROR: " + errorCode.toString();
        Message message = new Message(
                MessageType.SYNTAX_ERROR,
                new Object[]{0, 0, "", fatalText}
        );
        parser.sendMessage(message);
        System.exit(errorCode.getStatus());
    }
}
