package xianzhan.pascal.backend.interpreter;

import xianzhan.pascal.backend.Backend;
import xianzhan.pascal.frontend.Scanner;
import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalScanner;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Interface for the interactive source-level debugger.
 *
 * @author xianzhan
 * @since 2019-07-09
 */
public abstract class Debugger {

    private RuntimeStack     runtimeStack;
    private HashSet<Integer> breakpoints;
    private HashSet<String>  watchPoints;

    private Scanner commandInput;

    public Debugger(Backend backend, RuntimeStack runtimeStack) {
        this.runtimeStack = runtimeStack;
        backend.addMessageListener(new BackendMessageListener());

        breakpoints = new HashSet<>();
        watchPoints = new HashSet<>();

        // Create the command input from the standard input.
        try {
            commandInput = new PascalScanner(
                    new Source(new BufferedReader(new InputStreamReader(System.in)))
            );
        } catch (IOException ignore) {

        }
    }

    public RuntimeStack getRuntimeStack() {
        return runtimeStack;
    }

    private class BackendMessageListener implements MessageListener {

        /**
         * Called by the back end whenever it produces a message.
         *
         * @param message the message.
         */
        @Override
        public void messageReceived(Message message) {
            processMessage(message);
        }
    }

    /**
     * Read the debugger commands.
     */
    public void readCommands() {
        do {
            promptForCommand();
        } while (parseCommand());
    }

    /**
     * Return the current token from the command input.
     *
     * @return the token.
     * @throws Exception if an error occurred.
     */
    public Token currentToken() throws Exception {
        return commandInput.currentToken();
    }

    /**
     * Return the next token from the command input.
     *
     * @return the token.
     * @throws Exception if an error occurred.
     */
    public Token nextToken()
            throws Exception {
        return commandInput.nextToken();
    }

    /**
     * Get the next word token from the command input.
     *
     * @param errorMessage the error message if an exception is thrown.
     * @return the text of the word token.
     * @throws Exception if an error occurred.
     */
    public String getWord(String errorMessage) throws Exception {
        Token token = currentToken();
        TokenType type = token.getType();

        if (type == PascalTokenType.IDENTIFIER) {
            String word = token.getText().toLowerCase();
            nextToken();
            return word;
        } else {
            throw new Exception(errorMessage);
        }
    }

    /**
     * Get the next integer constant token from the command input.
     *
     * @param errorMessage the error message if an exception is thrown.
     * @return the constant integer value.
     * @throws Exception if an error occurred.
     */
    public Integer getInteger(String errorMessage) throws Exception {
        Token token = currentToken();
        TokenType type = token.getType();

        if (type == PascalTokenType.INTEGER) {
            Integer value = (Integer) token.getValue();
            nextToken();
            return value;
        } else {
            throw new Exception(errorMessage);
        }
    }

    /**
     * Get the next constant value token from the command input.
     *
     * @param errorMessage the error message if an exception is thrown.
     * @return the constant value.
     * @throws Exception if an error occurred.
     */
    public Object getValue(String errorMessage) throws Exception {
        Token token = currentToken();
        TokenType tokenType = token.getType();
        boolean sign = false;
        boolean minus = false;

        // Unary plus or minus sign.
        if ((tokenType == PascalTokenType.MINUS) | (tokenType == PascalTokenType.PLUS)) {
            sign = true;
            minus = tokenType == PascalTokenType.MINUS;
            token = nextToken();
            tokenType = token.getType();
        }

        switch ((PascalTokenType) tokenType) {

            case INTEGER: {
                Integer value = (Integer) token.getValue();
                nextToken();
                return minus ? -value : value;
            }

            case REAL: {
                Float value = (Float) token.getValue();
                nextToken();
                return minus ? -value : value;
            }

            case STRING: {
                if (sign) {
                    throw new Exception(errorMessage);
                } else {
                    String value = (String) token.getValue();
                    nextToken();
                    return value.charAt(0);
                }
            }

            case IDENTIFIER: {
                if (sign) {
                    throw new Exception(errorMessage);
                } else {
                    String name = token.getText();
                    nextToken();

                    if ("true".equalsIgnoreCase(name)) {
                        return true;
                    } else if ("false".equalsIgnoreCase(name)) {
                        return false;
                    } else {
                        throw new Exception(errorMessage);
                    }
                }
            }

            default: {
                throw new Exception(errorMessage);
            }
        }
    }

    /**
     * Skip the rest of this command input line.
     *
     * @throws Exception if an error occurred.
     */
    public void skipToNextCommand() throws Exception {
        commandInput.skipToNextLine();
    }

    /**
     * Set a breakpoint at a source line.
     *
     * @param lineNumber the source line number.
     */
    public void setBreakpoint(Integer lineNumber) {
        breakpoints.add(lineNumber);
    }

    /**
     * Remove a breakpoint at a source line.
     *
     * @param lineNumber the source line number.
     */
    public void unsetBreakpoint(Integer lineNumber) {
        breakpoints.remove(lineNumber);
    }

    /**
     * Check if a source line is at a breakpoint.
     *
     * @param lineNumber the source line number
     * @return true if at a breakpoint, else false.
     */
    public boolean isBreakpoint(Integer lineNumber) {
        return breakpoints.contains(lineNumber);
    }

    /**
     * Set a watchpoint on a variable.
     *
     * @param name the variable name.
     */
    public void setWatchpoint(String name) {
        watchPoints.add(name);
    }

    /**
     * Remove a watchpoint on a variable.
     *
     * @param name the variable name.
     */
    public void unsetWatchpoint(String name) {
        watchPoints.remove(name);
    }

    /**
     * Check if a variable is a watchpoint.
     *
     * @param name the variable name.
     * @return true if a watchpoint, else false.
     */
    public boolean isWatchpoint(String name) {
        return watchPoints.contains(name);
    }

    /**
     * Process a message from the back end.
     *
     * @param message the message.
     */
    public abstract void processMessage(Message message);

    /**
     * Display a prompt for a debugger command.
     */
    public abstract void promptForCommand();

    /**
     * Parse a debugger command.
     *
     * @return true to parse another command immediately, else false.
     */
    public abstract boolean parseCommand();

    /**
     * Process a source statement.
     *
     * @param lineNumber the statement line number.
     */
    public abstract void atStatement(Integer lineNumber);

    /**
     * Process a breakpoint at a statement.
     *
     * @param lineNumber the statement line number.
     */
    public abstract void atBreakpoint(Integer lineNumber);

    /**
     * Process the current value of a watchpoint variable.
     *
     * @param lineNumber the current statement line number.
     * @param name       the variable name.
     * @param value      the variable's value.
     */
    public abstract void atWatchpointValue(Integer lineNumber, String name, Object value);

    /**
     * Process the assigning a new value to a watchpoint variable.
     *
     * @param lineNumber the current statement line number.
     * @param name       the variable name.
     * @param value      the new value.
     */
    public abstract void atWatchpointAssignment(Integer lineNumber, String name, Object value);

    /**
     * Process calling a declared procedure or function.
     *
     * @param lineNumber  the current statement line number.
     * @param routineName the routine name.
     */
    public abstract void callRoutine(Integer lineNumber, String routineName);

    /**
     * Process returning from a declared procedure or function.
     *
     * @param lineNumber  the current statement line number.
     * @param routineName the routine name.
     */
    public abstract void returnRoutine(Integer lineNumber, String routineName);

    /**
     * Display a value.
     *
     * @param valueString the value string.
     */
    public abstract void displayValue(String valueString);

    /**
     * Display the call stack.
     *
     * @param stack the list of elements of the call stack.
     */
    public abstract void displayCallStack(ArrayList stack);

    /**
     * Terminate execution of the source program.
     */
    public abstract void quit();

    /**
     * Handle a debugger command error.
     *
     * @param errorMessage the error message.
     */
    public abstract void commandError(String errorMessage);

    /**
     * Handle a source program runtime error.
     *
     * @param errorMessage the error message.
     * @param lineNumber   the source line number where the error occurred.
     */
    public abstract void runtimeError(String errorMessage, Integer lineNumber);
}
