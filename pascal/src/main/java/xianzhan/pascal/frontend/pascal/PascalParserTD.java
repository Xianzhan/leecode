package xianzhan.pascal.frontend.pascal;

import xianzhan.pascal.frontend.EofToken;
import xianzhan.pascal.frontend.Parser;
import xianzhan.pascal.frontend.Scanner;
import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.parser.BlockParser;
import xianzhan.pascal.intermediate.ICode;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
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
 * @author xianzhan
 * @since 2019-05-08
 */
public class PascalParserTD extends Parser {

    protected static PascalErrorHandler errorHandler = new PascalErrorHandler();

    /**
     * name of the routine being parsed.
     */
    private SymTabEntry routineId;

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

        ICode iCode = ICodeFactory.createICode();
        Predefined.initialize(symTabStack);

        routineId = symTabStack.enterLocal("DummyProgramName".toLowerCase());
        routineId.setDefinition(DefinitionEnumImpl.PROGRAM);
        symTabStack.setProgramId(routineId);

        // Push a new symbol table onto the symbol table stack and set
        // the routine's symbol table and intermediate code.
        routineId.setAttribute(SymTabKeyImpl.ROUTINE_SYMTAB, symTabStack.push());
        routineId.setAttribute(SymTabKeyImpl.ROUTINE_ICODE, iCode);

        BlockParser blockParser = new BlockParser(this);

        try {
            Token token = nextToken();

            // Parse a block.
            ICodeNode rootNode = blockParser.parse(token, routineId);
            iCode.setRoot(rootNode);
            symTabStack.pop();

            // Look for the final period.
            token = currentToken();
            if (token.getType() != PascalTokenType.DOT) {
                errorHandler.flag(token, PascalErrorCode.MISSING_PERIOD, this);
            }
            token = currentToken();

            // Send the parser summary message.
            float elapsedTime = (System.currentTimeMillis() - startTime) / 1000F;
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
     * <h1>Error Recovery</h1>
     * <p>
     * What are a parser’s options for error recovery?
     *
     * <ol>
     * <li>
     * It can simply terminate after encountering a syntax error. In the worst cases, it can hang by getting stuck on a token that’s
     * never consumed or even crash. In other words, there is no error recovery at all. This option is easy for the compiler writer but
     * extremely annoying for programmers attempting to use the compiler.
     * </li>
     * <li>
     * It can become hopelessly lost but still attempt to parse the rest of the source program while spitting out sequences of irrelevant
     * error messages. There’s no error recovery here, either, but the compiler writer doesn’t want to admit it.
     * </li>
     * <li>
     * It can skip tokens after the erroneous one until it finds a token it recognizes and safely resume syntax checking the rest of the
     * source program.
     * </li>
     * </ol>
     * <p>
     * Clearly, the first two options are undesirable. To implement the third option, the parser must “synchronize” itself frequently at
     * tokens that it expects. Whenever there is a syntax error, the parser must find the next token in the source program where it can reliably
     * resume syntax checking. Ideally, it can find such a token as soon after the error as possible.
     * <p>
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

    public SymTabEntry getRoutineId() {
        return routineId;
    }
}
