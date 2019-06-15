package xianzhan.pascal.frontend.pascal;

import xianzhan.pascal.frontend.EofToken;
import xianzhan.pascal.frontend.Parser;
import xianzhan.pascal.frontend.Scanner;
import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.parser.StatementParser;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageType;

import java.util.EnumSet;

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
     * Constructor for subclasses.
     *
     * @param parent the parent parser.
     */
    public PascalParserTD(PascalParserTD parent) {
        super(parent.getScanner());
    }

    /**
     * Return the error handler.
     *
     * @return the error handler.
     */
    public PascalErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Parse a Pascal source program and generate the symbol table
     * and the intermediate code.
     *
     * @throws Exception if an error occurred.
     */
    @Override
    public void parse() throws Exception {

        long startTime = System.currentTimeMillis();
        iCode = ICodeFactory.createICode();

        try {
            Token token = nextToken();
            ICodeNode rootNode = null;

            // Look for the BEGIN token to parse a compound statement.
            if (token.getType() == PascalTokenType.BEGIN) {
                StatementParser statementParser = new StatementParser(this);
                rootNode = statementParser.parse(token);
                token = currentToken();
            } else {
                errorHandler.flag(token, PascalErrorCode.UNEXPECTED_TOKEN, this);
            }

            // Look for the final period.
            if (token.getType() != PascalTokenType.DOT) {
                errorHandler.flag(token, PascalErrorCode.MISSING_PERIOD, this);
            }
            token = currentToken();

            // Set the parse tree root node.
            if (rootNode != null) {
                iCode.setRoot(rootNode);
            }

//            // Loop over each token until the end of file.
///            while (!((token = nextToken()) instanceof EofToken)) {
///                TokenType tokenType = token.getType();
//
//                // Cross reference only the identifiers
///                if (tokenType == PascalTokenType.IDENTIFIER) {
//                    // Note that Pascal is not case-sensitive.
///                    String name = token.getText().toLowerCase();
//
//                    // If it's not already in the symbol table,
//                    // create and enter a new entry for the identifier.
///                    SymTabEntry entry = symTabStack.lookup(name);
///                    if (entry == null) {
///                        entry = symTabStack.enterLocal(name);
///                    }
//
//                    // Append the current line number to the entry.
///                    entry.appendLineNumber(token.getLineNumber());
//
//                    // Format each token.
////                    Message message = new Message(
////                            MessageType.TOKEN,
////                            new Object[]{
////                                    token.getLineNumber(),
////                                    token.getPosition(),
////                                    tokenType,
////                                    token.getText(),
////                                    token.getValue()
////                            }
////                    );
////                    sendMessage(message);
///                } else if (tokenType == PascalTokenType.ERROR) {
///                    errorHandler.flag(
///                            token,
///                            (PascalErrorCode) token.getValue(),
///                            this
///                    );
///                }
///            }

            // Send the parser summary message.
            float elapsedTime =
                    (System.currentTimeMillis() - startTime) / 1000F;
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

    /**
     * Synchronize the parser.
     *
     * @param syncSet the set of token types for synchronizing the parser.
     * @return the token where the parser has synchronized.
     * @throws Exception if an error occurred.
     */
    public Token synchronize(EnumSet syncSet) throws Exception {
        Token token = currentToken();

        // If the current token is not in the synchronization set,
        // then it is unexpected and the parser must recover.
        if (!syncSet.contains(token.getType())) {

            // Flag the unexpected token.
            errorHandler.flag(token, PascalErrorCode.UNEXPECTED_TOKEN, this);

            // Recover by skipping tokens that are not
            // in the synchronization set.
            do {
                token = nextToken();
            } while (!(token instanceof EofToken) && !syncSet.contains(token.getType()));
        }

        return token;
    }
}
