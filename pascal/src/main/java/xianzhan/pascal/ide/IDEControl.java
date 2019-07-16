package xianzhan.pascal.ide;

import java.awt.Color;

/**
 * The master interface of the Pascal IDE.
 *
 * @author xianzhan
 * @since 2019-07-10
 */
public interface IDEControl {

    // Debugger output line tags.

    String LISTING_TAG     = "!LISTING:";
    String PARSER_TAG      = "!PARSER:";
    String SYNTAX_TAG      = "!SYNTAX:";
    String INTERPRETER_TAG = "!INTERPRETER:";

    String DEBUGGER_AT_TAG       = "!DEBUGGER.AT:";
    String DEBUGGER_BREAK_TAG    = "!DEBUGGER.BREAK:";
    String DEBUGGER_ROUTINE_TAG  = "!DEBUGGER.ROUTINE:";
    String DEBUGGER_VARIABLE_TAG = "!DEBUGGER.VARIABLE:";

    /**
     * Set the path of the source file.
     *
     * @param sourcePath the path.
     */
    void setSourcePath(String sourcePath);

    /**
     * @return the path of the source file.
     */
    String getSourcePath();

    /**
     * Set the path of the runtime input data file.
     *
     * @param inputPath the path.
     */
    void setInputPath(String inputPath);

    /**
     * @return the path of the runtime input data file.
     */
    String getInputPath();

    /**
     * Start the debugger process.
     *
     * @param sourceName the source file name.
     */
    void startDebuggerProcess(String sourceName);

    /**
     * Stop the debugger process.
     */
    void stopDebuggerProcess();

    /**
     * Send a command or runtime input text to the debugger process.
     *
     * @param text the command string or input text.
     */
    void sendToDebuggerProcess(String text);

    /**
     * Set the editor window's message.
     *
     * @param message the message.
     * @param color   the message color.
     */
    void setEditWindowMessage(String message, Color color);

    /**
     * Clear the editor window's syntax errors.
     */
    void clearEditWindowErrors();

    /**
     * Add a syntax error message to the editor window's syntax errors.
     *
     * @param line the error message.
     */
    void addToEditWindowErrors(String line);

    /**
     * Show the debugger window.
     *
     * @param sourceName the source file name.
     */
    void showDebugWindow(String sourceName);

    /**
     * Clear the debugger window's listing.
     */
    void clearDebugWindowListing();

    /**
     * Add a line to the debugger window's listing.
     *
     * @param line the listing line.
     */
    void addToDebugWindowListing(String line);

    /**
     * Select a listing line in the debugger window.
     *
     * @param lineNumber the line number.
     */
    void selectDebugWindowListingLine(int lineNumber);

    /**
     * Set the debugger to a listing line.
     *
     * @param lineNumber the line number.
     */
    void setDebugWindowAtListingLine(int lineNumber);

    /**
     * Set the debugger to break at a listing line.
     *
     * @param lineNumber the line number.
     */
    void breakDebugWindowAtListingLine(int lineNumber);

    /**
     * Set the debugger window's message.
     *
     * @param message the message.
     * @param color   the message color.
     */
    void setDebugWindowMessage(String message, Color color);

    /**
     * Stop the debugger.
     */
    void stopDebugWindow();

    /**
     * Show the call stack window.
     *
     * @param sourceName the source file name.
     */
    void showCallStackWindow(String sourceName);

    /**
     * Initialize the call stack display.
     */
    void initializeCallStackWindow();

    /**
     * Add an invoked routine to the call stack display.
     *
     * @param level  the routine's nesting level.
     * @param header the routine's header.
     */
    void addRoutineToCallStackWindow(String level, String header);

    /**
     * Add a local variable to the call stack display.
     *
     * @param name  the variable's name.
     * @param value the variable's value.
     */
    void addVariableToCallStackWindow(String name, String value);

    /**
     * Complete the call stack display.
     */
    void completeCallStackWindow();

    /**
     * Show the console window.
     *
     * @param sourceName the source file name.
     */
    void showConsoleWindow(String sourceName);

    /**
     * Clear the console window's output.
     */
    void clearConsoleWindowOutput();

    /**
     * Add output text to the console window.
     *
     * @param text the output text.
     */
    void addToConsoleWindowOutput(String text);

    /**
     * Enable runtime input from the console window.
     */
    void enableConsoleWindowInput();

    /**
     * Disable runtime input from the console window.
     */
    void disableConsoleWindowInput();
}
