package xianzhan.pascal.ide;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * The debugger process of the Pascal IDE.
 *
 * @author xianzhan
 * @since 2019-07-11
 */
public class DebuggerProcess extends Thread {
    /**
     * the IDE control interface
     */
    private IDEControl     control;
    /**
     * the debugger process
     */
    private Process        process;
    /**
     * source file name
     */
    private String         sourceName;
    /**
     * IDE to debugger I/O stream
     */
    private PrintWriter    toDebuggerStream;
    /**
     * debugger process output
     */
    private DebuggerOutput debuggerOutput;
    /**
     * true if have syntax errors
     */
    private boolean        haveSyntaxErrors;
    /**
     * true if debugging process I/O
     */
    private boolean        debugging;

    /**
     * Constructor.
     *
     * @param control    the IDE control.
     * @param sourceName the source file name.
     */
    public DebuggerProcess(IDEControl control, String sourceName) {
        this.control = control;
        this.sourceName = sourceName;
    }

    /**
     * The command that starts the debugger process.
     */
    private static final String COMMAND = "java -classpath classes Pascal execute %s %s";

    /**
     * Run the procecess.
     */
    @Override
    public void run() {
        try {
            // Start the Pascal debugger process.
            String[] executes = List.of(
                    System.getProperty("java.home") + "\\bin\\java.exe",
                    "-classpath",
                    System.getProperty("java.class.path"),
                    "xianzhan.pascal.Pascal",
                    "execute",
                    control.getSourcePath(),
                    control.getInputPath()
            ).toArray(new String[0]);

            process = Runtime.getRuntime().exec(executes);

            // Capture the process's input stream.
            toDebuggerStream = new PrintWriter(process.getOutputStream());

            // Read and dispatch output text from the
            // debugger process for processing.
            debuggerOutput = new DebuggerOutput(process);
            dispatchDebuggerOutput();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Kill the debugger process.
     */
    public void kill() {
        if (process != null) {
            process.destroy();
            process = null;
        }
    }

    /**
     * Write a command or runtime input text
     * to the debugger process's standard input.
     *
     * @param text the command string or input text.
     */
    public void writeToDebuggerStandardInput(String text) {
        synchronized (DebuggerProcess.class) {
            toDebuggerStream.println(text);
            toDebuggerStream.flush();

            if (debugging) {
                System.err.println("Sent: '" + text + "'");
            }
        }
    }

    /**
     * Read and dispatch output text from the debugger process for processing.
     *
     * @throws Exception if an error occurred.
     */
    private void dispatchDebuggerOutput() throws Exception {
        String text;

        // Loop to process debugger output text
        // which may contain embedded output tags.
        do {
            text = debuggerOutput.next();
            if (debugging) {
                System.out.println("Read: '" + text + "'");
            }

            int index;
            do {
                Thread.sleep(1);
                index = text.indexOf('!');

                // The debugger output text contains the ! character.
                // It may be the start of an output tag.
                if (index >= 0) {

                    // Add any preceding text to the console window.
                    if (index > 0) {
                        String precedingText = text.substring(0, index);
                        control.addToConsoleWindowOutput(precedingText);
                        text = text.substring(index);
                    }

                    // Yes, it was an output tag. Don't loop again.
                    if (processTag(text)) {
                        index = -1;
                    }

                    // No, it wasn't.
                    // Loop again to process the rest of the output text.
                    else {
                        control.addToConsoleWindowOutput("!");
                        text = text.substring(1);
                    }
                }

                // Send all the debugger output text to the console window.
                else {
                    control.addToConsoleWindowOutput(text);
                }
            } while (index >= 0);
        }
        while (!text.startsWith(IDEControl.INTERPRETER_TAG));
    }

    /**
     * Process a tag in the output text.
     *
     * @param text the output text
     * @return true if processed a tag, else false.
     */
    private boolean processTag(String text) {
        // Listing line.
        if (text.startsWith(IDEControl.LISTING_TAG)) {
            control.addToDebugWindowListing(text.substring(IDEControl.LISTING_TAG.length()));
            return true;
        }

        // Syntax error message.
        else if (text.startsWith(IDEControl.SYNTAX_TAG)) {
            String errorMessage = text.substring(IDEControl.SYNTAX_TAG.length());
            control.addToEditWindowErrors(errorMessage);
            haveSyntaxErrors = true;
            return true;
        }

        // Parser message.
        else if (text.startsWith(IDEControl.PARSER_TAG)) {
            control.setEditWindowMessage(text.substring(IDEControl.PARSER_TAG.length()),
                    haveSyntaxErrors
                            ? Color.RED
                            : Color.BLUE);

            if (!haveSyntaxErrors) {
                control.clearEditWindowErrors();
                control.setDebugWindowMessage("", Color.BLUE);
                control.showDebugWindow(sourceName);
                control.showCallStackWindow(sourceName);
                control.showConsoleWindow(sourceName);
            } else {
                control.setDebugWindowMessage("Fix syntax errors.", Color.RED);
                control.stopDebugWindow();
                control.disableConsoleWindowInput();
            }

            return true;
        }

        // Debugger at a source statement.
        else if (text.startsWith(IDEControl.DEBUGGER_AT_TAG)) {
            String lineNumber = text.substring(IDEControl.DEBUGGER_AT_TAG.length());
            control.setDebugWindowAtListingLine(Integer.parseInt(lineNumber.trim()));
            control.setDebugWindowMessage(" ", Color.BLUE);
            return true;
        }

        // Debugger break at a source statement.
        else if (text.startsWith(IDEControl.DEBUGGER_BREAK_TAG)) {
            String lineNumber = text.substring(IDEControl.DEBUGGER_BREAK_TAG.length());
            control.breakDebugWindowAtListingLine(Integer.parseInt(lineNumber.trim()));
            control.setDebugWindowMessage("Break at text " + lineNumber, Color.BLUE);
            return true;
        }

        // Debugger add a routine to the call stack.
        else if (text.startsWith(IDEControl.DEBUGGER_ROUTINE_TAG)) {
            String[] components = text.split(":");
            String level = components[1].trim();

            // Header.
            if ("-1".equals(level)) {
                control.initializeCallStackWindow();
            }

            // Footer.
            else if ("-2".equals(level)) {
                control.completeCallStackWindow();
            }

            // Routine name.
            else {
                String header = components[2].trim();
                control.addRoutineToCallStackWindow(level, header);
            }

            return true;
        }

        // Debugger add a local variable to the call stack.
        else if (text.startsWith(IDEControl.DEBUGGER_VARIABLE_TAG)) {
            text = text.substring(IDEControl.DEBUGGER_VARIABLE_TAG.length());

            int index = text.indexOf(":");
            String name = text.substring(0, index);
            String value = text.substring(index + 1);

            control.addVariableToCallStackWindow(name, value);
            return true;
        }

        // Interpreter message.
        else if (text.startsWith(IDEControl.INTERPRETER_TAG)) {
            control.setDebugWindowMessage(text.substring(IDEControl.INTERPRETER_TAG.length()), Color.BLUE);
            control.stopDebugWindow();
            control.disableConsoleWindowInput();
            return true;
        } else {
            // it wasn't an output tag
            return false;
        }
    }

    /**
     * Output from the debugger.
     */
    private class DebuggerOutput {
        private static final int BUFFER_SIZE = 1024;

        /**
         * debugger to IDE I/O stream
         */
        private BufferedReader fromDebuggerStream;
        /**
         * output buffer
         */
        private char[]         buffer;
        /**
         * start of output line
         */
        private int            start;
        /**
         * index of \n or end of line
         */
        private int            index;
        /**
         * previous index
         */
        private int            prevIndex;
        /**
         * output text length
         */
        private int            length;

        /**
         * Constructor.
         *
         * @param process the interpreter process.
         */
        private DebuggerOutput(Process process) {
            fromDebuggerStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
            buffer = new char[BUFFER_SIZE];
            start = 0;
            length = 0;
        }

        /**
         * Get the next complete or partial output line.
         *
         * @return the output line.
         * @throws Exception if an error occurred.
         */
        private String next() throws Exception {
            String output = "";

            // Loop to process output from the interpreter.
            for (; ; ) {
                Thread.sleep(1);
                index = findEol(prevIndex);

                // Found end of line: Return the line.
                if (index < length) {
                    output += new String(buffer, start, index - start + 1);
                    start = index + 1;
                    prevIndex = start;

                    if (debugging) {
                        System.err.println("Output: '" + output + "'");
                    }

                    return output;
                }

                // No end of line: Append to the current output.
                if (index > start) {
                    output += new String(buffer, start, index - start);
                }

                // Prepare to read again into the buffer.
                start = 0;
                length = 0;
                prevIndex = 0;

                // Read more output if available.
                if (fromDebuggerStream.ready()) {
                    length = readFromDebuggerStandardOutput();
                }

                // No more output: Return the current output.
                else {
                    if (debugging) {
                        System.err.println("Output: '" + output + "'");
                    }

                    return output;
                }
            }
        }

        /**
         * Read debugger status or runtime output
         * from the debugger's standard output.
         *
         * @return the number of characters read.
         * @throws Exception if an error occurred.
         */
        private int readFromDebuggerStandardOutput() throws Exception {
            return fromDebuggerStream.read(buffer);
        }

        /**
         * Look for \n in the output.
         *
         * @param index where to start looking.
         * @return the index of \n or the end of output.
         */
        private int findEol(int index) {
            while ((index < length) && (buffer[index] != '\n')) {
                ++index;
            }

            return index;
        }
    }
}
