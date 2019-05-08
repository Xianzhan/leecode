package xianzhan.pascal.frontend;

import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageHandler;
import xianzhan.pascal.message.MessageListener;
import xianzhan.pascal.message.MessageProducer;
import xianzhan.pascal.message.MessageType;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * <h1>Source</h1>
 *
 * <p>The framework class that represents the source program.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 *
 * @author Ronald Mak
 */
public class Source implements MessageProducer {
    /**
     * end-of-line character
     */
    public static final char EOL = '\n';
    /**
     * end-of-file character
     */
    public static final char EOF = (char) 0;

    /**
     * reader for the source program
     */
    private BufferedReader reader;
    /**
     * source line
     */
    private String         line;
    /**
     * current source line number
     */
    private int            lineNum;
    /**
     * current source line position
     */
    private int            currentPos;

    /**
     * delegate to handle messages
     */
    private MessageHandler messageHandler;

    /**
     * Constructor.
     *
     * @param reader the reader for the source program
     * @throws IOException if an I/O error occurred
     */
    public Source(BufferedReader reader) throws IOException {
        this.lineNum = 0;
        // set to -2 to read the first source line
        this.currentPos = -2;
        this.reader = reader;
        this.messageHandler = new MessageHandler();
    }

    /**
     * Getter.
     *
     * @return the current source line number.
     */
    public int getLineNum() {
        return lineNum;
    }

    /**
     * Getter.
     *
     * @return the position of the next source character in the
     * current source line.
     */
    public int getPosition() {
        return currentPos;
    }

    /**
     * Return the source character at the current position.
     *
     * @return the source character at the current position.
     * @throws Exception if an error occurred.
     */
    public char currentChar() throws Exception {
        // First time?
        if (currentPos == -2) {
            readLine();
            return nextChar();
        }

        // At end of file?
        else if (line == null) {
            return EOF;
        }

        // At end of line?
        else if ((currentPos == -1) || (currentPos == line.length())) {
            return EOL;
        }

        // Need to read the next line?
        else if (currentPos > line.length()) {
            readLine();
            return nextChar();
        }

        // Return the character at the current position.
        else {
            return line.charAt(currentPos);
        }
    }

    /**
     * Consume the current source character and return the next character.
     *
     * @return the next source character.
     * @throws Exception if an error occurred.
     */
    public char nextChar() throws Exception {
        ++currentPos;
        return currentChar();
    }

    /**
     * Return the source character following the current character without
     * consuming the current character.
     *
     * @return the following character.
     * @throws Exception if an error occurred.
     */
    public char peekChar() throws Exception {
        currentChar();
        if (line == null) {
            return EOF;
        }

        int nextPos = currentPos + 1;
        return nextPos < line.length() ? line.charAt(nextPos) : EOL;
    }

    /**
     * Read the next source line.
     *
     * @throws IOException if an I/O error occurred.
     */
    private void readLine() throws IOException {
        // null when at the end of the source
        line = reader.readLine();
        currentPos = -1;

        if (line != null) {
            ++lineNum;
        }

        // Send a source line message containing the line number
        // and the line text to all the listeners.
        if (line != null) {
            Message message = new Message(
                    MessageType.SOURCE_LINE,
                    new Object[]{lineNum, line}
            );
            sendMessage(message);
        }
    }

    /**
     * Close the source.
     *
     * @throws Exception if an error occurred.
     */
    public void close() throws Exception {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
    }

    /**
     * Add a parser message listener.
     *
     * @param listener the message listener to add.
     */
    @Override
    public void addMessageListener(MessageListener listener) {
        messageHandler.addListener(listener);
    }

    /**
     * Remove a parser message listener.
     *
     * @param listener the message listener to remove.
     */
    @Override
    public void removeMessageListener(MessageListener listener) {
        messageHandler.removeListener(listener);
    }

    /**
     * Notify listeners after setting the message.
     *
     * @param message the message to set.
     */
    @Override
    public void sendMessage(Message message) {
        messageHandler.sendMessage(message);
    }
}
