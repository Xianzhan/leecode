package xianzhan.pascal;

import xianzhan.pascal.backend.Backend;
import xianzhan.pascal.backend.BackendFactory;
import xianzhan.pascal.frontend.FrontendFactory;
import xianzhan.pascal.frontend.Parser;
import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.intermediate.ICode;
import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageListener;
import xianzhan.pascal.message.MessageType;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Pascal
 * <p>
 * Compile or interpret a Pascal source program.
 *
 * @author Ronald Mak
 */
public class Pascal {
    /**
     * language-independent parser
     */
    private Parser  parser;
    /**
     * language-independent scanner
     */
    private Source  source;
    /**
     * generated intermediate code
     */
    private ICode   iCode;
    /**
     * generated symbol table
     */
    private SymTab  symTab;
    /**
     * backend
     */
    private Backend backend;

    /**
     * Compile or interpret a Pascal source program.
     *
     * @param operation either "compile" or "execute".
     * @param filePath  the source file path.
     * @param flags     the command line flags.
     */
    public Pascal(String operation, String filePath, String flags) {
        try {
            boolean intermediate = flags.indexOf('i') > -1;
            boolean xref = flags.indexOf('x') > -1;

            source = new Source(new BufferedReader(new FileReader(filePath)));
            source.addMessageListener(new SourceMessageListener());

            parser = FrontendFactory.createParser("Pascal", "top-down", source);
            parser.addMessageListener(new ParserMessageListener());

            backend = BackendFactory.createBackend(operation);
            backend.addMessageListener(new BackendMessageListener());

            parser.parse();
            source.close();

            iCode = parser.getICode();
            symTab = parser.getSymTab();

            backend.process(iCode, symTab);
        } catch (Exception ex) {
            System.out.println("***** Internal translator error. *****");
            ex.printStackTrace();
        }
    }

    private static final String FLAGS = "[-ix]";
    private static final String USAGE =
            "Usage: Pascal execute|compile " + FLAGS + " <source file path>";

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
            if (!(operation.equalsIgnoreCase("compile")
                  || operation.equalsIgnoreCase("execute"))) {
                throw new Exception();
            }

            int i = 0;
            StringBuilder flags = new StringBuilder();

            // Flags.
            while ((++i < args.length) && (args[i].charAt(0) == '-')) {
                flags.append(args[i].substring(1));
            }

            // Source path.
            if (i < args.length) {
                String path = args[i];
                new Pascal(operation, path, flags.toString());
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println(USAGE);
        }
    }

    private static final String SOURCE_LINE_FORMAT = "%03d %s";

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

                    System.out.println(String.format(SOURCE_LINE_FORMAT,
                                                     lineNumber,
                                                     lineText));
                    break;
                }
                default:
            }
        }
    }

    private static final String PARSER_SUMMARY_FORMAT =
            "\n%,20d source lines." +
            "\n%,20d syntax errors." +
            "\n%,20.2f seconds total parsing time.\n";

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

                    System.out.printf(PARSER_SUMMARY_FORMAT,
                                      statementCount,
                                      syntaxErrors,
                                      elapsedTime);
                    break;
                }
                default:
            }
        }
    }

    private static final String INTERPRETER_SUMMARY_FORMAT =
            "\n%,20d statements executed." +
            "\n%,20d runtime errors." +
            "\n%,20.2f seconds total execution time.\n";

    private static final String COMPILER_SUMMARY_FORMAT =
            "\n%,20d instructions generated." +
            "\n%,20.2f seconds total code generation time.\n";

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

                case INTERPRETER_SUMMARY: {
                    Number[] body = (Number[]) message.getBody();
                    int executionCount = (Integer) body[0];
                    int runtimeErrors = (Integer) body[1];
                    float elapsedTime = (Float) body[2];

                    System.out.printf(INTERPRETER_SUMMARY_FORMAT,
                                      executionCount,
                                      runtimeErrors,
                                      elapsedTime);
                    break;
                }

                case COMPILER_SUMMARY: {
                    Number[] body = (Number[]) message.getBody();
                    int instructionCount = (Integer) body[0];
                    float elapsedTime = (Float) body[1];

                    System.out.printf(COMPILER_SUMMARY_FORMAT,
                                      instructionCount, elapsedTime);
                    break;
                }
                default:
            }
        }
    }
}
