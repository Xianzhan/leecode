package xianzhan.pascal;

import xianzhan.pascal.backend.Backend;
import xianzhan.pascal.backend.BackendFactory;
import xianzhan.pascal.frontend.FrontendFactory;
import xianzhan.pascal.frontend.Parser;
import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.ide.IDEControl;
import xianzhan.pascal.intermediate.ICode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.SymTabStack;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageListener;
import xianzhan.pascal.message.MessageType;
import xianzhan.pascal.util.CrossReferencer;
import xianzhan.pascal.util.ParseTreePrinter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Pascal
 * <p>
 * Compile or interpret a Pascal source program.
 *
 * @author xianzhan
 * @since 2019-05-08
 */
public class Pascal {
    /**
     * language-independent parser
     */
    private Parser      parser;
    /**
     * language-independent scanner
     */
    private Source      source;
    /**
     * generated intermediate code
     */
    private ICode       iCode;
    /**
     * symbol table stack
     */
    private SymTabStack symTabStack;
    /**
     * backend
     */
    private Backend     backend;

    // ---------- flag -----------

    /**
     * true to print intermediate code
     */
    private boolean intermediate;
    /**
     * true to print cross-reference listing
     */
    private boolean xref;
    /**
     * true to print source line tracing
     */
    private boolean lines;
    /**
     * true to print value assignment tracing
     */
    private boolean assign;
    /**
     * true to print value fetch tracing
     */
    private boolean fetch;
    /**
     * true to print routine call tracing
     */
    private boolean call;
    /**
     * true to print routine return tracing
     */
    private boolean printReturn;

    /**
     * Compile or interpret a Pascal source program.
     *
     * @param operation  either "compile" or "execute".
     * @param sourcePath the source file path.
     * @param inputPath  the input file path.
     * @param flags      the command line flags.
     */
    public Pascal(String operation, String sourcePath, String inputPath, String flags) {
        try {
            intermediate = flags.indexOf('i') > -1;
            xref = flags.indexOf('x') > -1;
            lines = flags.indexOf('l') > -1;
            assign = flags.indexOf('a') > -1;
            fetch = flags.indexOf('f') > -1;
            call = flags.indexOf('c') > -1;
            printReturn = flags.indexOf('r') > -1;

            source = new Source(new BufferedReader(new FileReader(sourcePath)));
            source.addMessageListener(new SourceMessageListener());

            parser = FrontendFactory.createParser("Pascal", "top-down", source);
            parser.addMessageListener(new ParserMessageListener());

            backend = BackendFactory.createBackend(operation, inputPath);
            backend.addMessageListener(new BackendMessageListener());

            parser.parse();
            source.close();

            if (parser.getErrorCount() == 0) {
                symTabStack = parser.getSymTabStack();

                SymTabEntry programId = symTabStack.getProgramId();
                iCode = (ICode) programId.getAttribute(SymTabKeyImpl.ROUTINE_ICODE);

                if (xref) {
                    CrossReferencer crossReferencer = new CrossReferencer();
                    crossReferencer.print(symTabStack);
                }

                if (intermediate) {
                    ParseTreePrinter treePrinter = new ParseTreePrinter(System.out);
                    treePrinter.print(symTabStack);
                }

                backend.process(iCode, symTabStack);
            }
        } catch (Exception ex) {
            System.out.println("***** Internal translator error. *****");
            ex.printStackTrace();
        }
    }

    /**
     * –i (intermediate) and  –x (cross-reference)
     */
    private static final String FLAGS = "[-ixlafcr]";
    private static final String USAGE =
            "Usage: Pascal execute|compile " + FLAGS + " <source file path> [ <input file path> ]";

    /**
     * The main method.
     *
     * @param args command-line arguments: "compile" or "execute" followed by
     *             optional flags followed by the source file path.
     */
    public static void main(String[] args) {
        try {
            String operation = args[0];

            // Operation.
            if (!("compile".equalsIgnoreCase(operation) || "execute".equalsIgnoreCase(operation))) {
                throw new Exception();
            }

            int i = 0;
            StringBuilder flags = new StringBuilder();

            // Flags.
            while ((++i < args.length) && (args[i].charAt(0) == '-')) {
                flags.append(args[i].substring(1));
            }

            String sourcePath;
            String inputPath = null;

            // Source path.
            if (i < args.length) {
                sourcePath = args[i];
            } else {
                throw new Exception();
            }

            // Runtime input data file path.
            if (++i < args.length) {
                inputPath = args[i];

                File inputFile = new File(inputPath);
                if (!inputFile.exists()) {
                    System.out.println("Input file '" + inputPath + "' does not exist.");
                    throw new Exception();
                }
            }

            new Pascal(operation, sourcePath, inputPath, flags.toString());
        } catch (Exception ex) {
            System.out.println(USAGE);
        }
    }

    private static final String SOURCE_LINE_FORMAT = IDEControl.LISTING_TAG + "%03d %s";

    /**
     * Listener for source messages.
     */
    private class SourceMessageListener implements MessageListener {
        /**
         * Called by the source whenever it produces a message.
         *
         * @param message the message.
         */
        @Override
        public void messageReceived(Message message) {
            MessageType type = message.getType();
            Object[] body = (Object[]) message.getBody();

            switch (type) {

                case SOURCE_LINE: {
                    int lineNumber = (Integer) body[0];
                    String lineText = (String) body[1];

                    System.out.println(String.format(SOURCE_LINE_FORMAT, lineNumber, lineText));
                    break;
                }
                default:
            }
        }
    }

    private static final String PARSER_SUMMARY_FORMAT =
            IDEControl.PARSER_TAG + "%,d source lines, %,d syntax errors, " +
                    "%,.2f seconds total parsing time.\n";

    private static final int PREFIX_WIDTH = 5;

    /**
     * Listener for parser messages.
     */
    private class ParserMessageListener implements MessageListener {
        /**
         * Called by the parser whenever it produces a message.
         *
         * @param message the message.
         */
        @Override
        public void messageReceived(Message message) {
            MessageType type = message.getType();

            switch (type) {

                case PARSER_SUMMARY: {
                    Number[] body = (Number[]) message.getBody();
                    int statementCount = (Integer) body[0];
                    int syntaxErrors = (Integer) body[1];
                    float elapsedTime = (Float) body[2];

                    System.out.printf(PARSER_SUMMARY_FORMAT, statementCount, syntaxErrors, elapsedTime);
                    break;
                }

                case SYNTAX_ERROR: {
                    Object[] body = (Object[]) message.getBody();
                    int lineNumber = (Integer) body[0];
                    int position = (Integer) body[1];
                    String tokenText = (String) body[2];
                    String errorMessage = (String) body[3];

                    StringBuilder flagBuffer = new StringBuilder();

                    flagBuffer.append(IDEControl.SYNTAX_TAG);
                    flagBuffer.append(String.format("%d: %s", lineNumber, errorMessage));

                    // Text, if any, of the bad token.
                    if (tokenText != null) {
                        flagBuffer.append(" [at \"")
                                .append(tokenText)
                                .append("\"]");
                    }

                    System.out.println(flagBuffer.toString());
                    break;
                }
                default:
            }
        }
    }

    private static final String INTERPRETER_SUMMARY_FORMAT =
            IDEControl.INTERPRETER_TAG + "%,d statements executed, %,d runtime errors, " +
                    "%,.2f seconds total execution time.\n";

    private static final String COMPILER_SUMMARY_FORMAT =
            "\n%,20d instructions generated." +
                    "\n%,20.2f seconds total code generation time.\n";

    private static final String LINE_FORMAT =
            ">>> AT LINE %03d\n";

    private static final String ASSIGN_FORMAT =
            ">>> LINE %03d: %s = %s\n";

    private static final String FETCH_FORMAT =
            ">>> AT LINE %03d: %s : %s\n";

    private static final String CALL_FORMAT =
            ">>> AT LINE %03d: CALL %s\n";

    private static final String RETURN_FORMAT =
            ">>> AT LINE %03d: RETURN FROM %s\n";

    /**
     * Listener for back end messages.
     */
    private class BackendMessageListener implements MessageListener {

        /**
         * Called by the back end whenever it produces a message.
         *
         * @param message the message.
         */
        @Override
        public void messageReceived(Message message) {
            MessageType type = message.getType();

            switch (type) {
                case SOURCE_LINE: {
                    if (lines) {
                        int lineNumber = (int) message.getBody();
                        System.out.printf(LINE_FORMAT, lineNumber);
                    }
                    break;
                }

                case ASSIGN: {
                    if (assign) {
                        Object[] body = (Object[]) message.getBody();
                        int lineNumber = (int) body[0];
                        String variableName = (String) body[1];
                        Object value = body[2];

                        System.out.printf(ASSIGN_FORMAT, lineNumber, variableName, value);
                    }
                    break;
                }

                case FETCH: {
                    if (fetch) {
                        Object[] body = (Object[]) message.getBody();
                        int lineNumber = (Integer) body[0];
                        String variableName = (String) body[1];
                        Object value = body[2];

                        System.out.printf(FETCH_FORMAT, lineNumber, variableName, value);
                    }
                    break;
                }

                case CALL: {
                    if (call) {
                        Object[] body = (Object[]) message.getBody();
                        int lineNumber = (Integer) body[0];
                        String routineName = (String) body[1];

                        System.out.printf(CALL_FORMAT, lineNumber, routineName);
                    }
                    break;
                }

                case RETURN: {
                    if (printReturn) {
                        Object[] body = (Object[]) message.getBody();
                        int lineNumber = (Integer) body[0];
                        String routineName = (String) body[1];

                        System.out.printf(RETURN_FORMAT, lineNumber, routineName);
                    }
                    break;
                }

                case RUNTIME_ERROR: {
                    Object[] body = (Object[]) message.getBody();
                    String errorMessage = (String) body[0];
                    Integer lineNumber = (Integer) body[1];

                    System.out.print("*** RUNTIME ERROR");
                    if (lineNumber != null) {
                        System.out.print(String.format(" AT LINE %03d", lineNumber));
                    }
                    System.out.println(": " + errorMessage);
                    break;
                }

                case INTERPRETER_SUMMARY: {
                    Number[] body = (Number[]) message.getBody();
                    int executionCount = (Integer) body[0];
                    int runtimeErrors = (Integer) body[1];
                    float elapsedTime = (Float) body[2];

                    System.out.printf(INTERPRETER_SUMMARY_FORMAT, executionCount, runtimeErrors, elapsedTime);
                    break;
                }

                case COMPILER_SUMMARY: {
                    Number[] body = (Number[]) message.getBody();
                    int instructionCount = (Integer) body[0];
                    float elapsedTime = (Float) body[1];

                    System.out.printf(COMPILER_SUMMARY_FORMAT, instructionCount, elapsedTime);
                    break;
                }
                default:
            }
        }
    }
}
