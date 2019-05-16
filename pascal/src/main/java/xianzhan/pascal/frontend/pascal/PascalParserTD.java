package xianzhan.pascal.frontend.pascal;

import xianzhan.pascal.frontend.EofToken;
import xianzhan.pascal.frontend.Parser;
import xianzhan.pascal.frontend.Scanner;
import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageType;

/**
 * A parser must be able to handle syntax errors in the source program.
 * Error handling is a three-step process:
 * 1. Detection. Detect the presence of a syntax error.
 * 2. Flagging. Flag the error by pointing it out or highlighting it, and
 * display a descriptive error message.
 * 3. Recovery. Move past the error and resume parsing.
 *
 * @author Ronald Mak
 */
public class PascalParserTD extends Parser {

    protected static PascalErrorHandler errorHandler = new PascalErrorHandler();

    /**
     * Constructor.
     *
     * @param scanner the scanner to be used with this parser.
     */
    public PascalParserTD(Scanner scanner) {
        super(scanner);
    }

    /**
     * Parse a Pascal source program and generate the symbol table
     * and the intermediate code.
     */
    @Override
    public void parse() throws Exception {
        Token token;
        long startTime = System.currentTimeMillis();

        try {
            // Loop over each token until the end of file.
            while (!((token = nextToken()) instanceof EofToken)) {
                TokenType tokenType = token.getType();

                if (tokenType != PascalTokenType.ERROR) {

                    // Format each token.
                    Message message = new Message(
                            MessageType.TOKEN,
                            new Object[]{
                                    token.getLineNumber(),
                                    token.getPosition(),
                                    tokenType,
                                    token.getText(),
                                    token.getValue()
                            }
                    );
                    sendMessage(message);
                } else {
                    errorHandler.flag(
                            token,
                            (PascalErrorCode) token.getValue(),
                            this
                    );
                }
            }

            // Send the parser summary message.
            float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
            Message message = new Message(
                    MessageType.PARSER_SUMMARY,
                    new Number[]{
                            token.getLineNumber(),
                            getErrorCount(),
                            elapsedTime
                    }
            );
            sendMessage(message);
        } catch (java.io.IOException ex) {
            errorHandler.abortTranslation(PascalErrorCode.IO_ERROR, this);
        }
    }

    /**
     * Return the number of syntax errors found by the parser.
     *
     * @return the error count.
     */
    @Override
    public int getErrorCount() {
        return errorHandler.getErrorCount();
    }
}
