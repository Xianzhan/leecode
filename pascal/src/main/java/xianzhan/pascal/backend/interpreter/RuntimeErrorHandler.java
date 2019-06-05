package xianzhan.pascal.backend.interpreter;

import xianzhan.pascal.backend.Backend;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageType;

/**
 * Runtime error handler for the backend interpreter.
 *
 * @author xianzhan
 * @since 2019-06-05
 */
public class RuntimeErrorHandler {

    private static final int MAX_ERRORS = 5;

    /**
     * count of runtime errors
     */
    private static int errorCount = 0;

    /**
     * Return the count of runtime errors.
     *
     * @return the count of runtime errors.
     */
    public static int getErrorCount() {
        return errorCount;
    }

    public void flag(ICodeNode node,
                     RuntimeErrorCode errorCode,
                     Backend backend) {
        String lineNumber = null;

        // Look for the ancestor statement node with a line number attribute.
        while ((node != null) && (node.getAttribute(ICodeKeyEnumImpl.LINE) == null)) {
            node = node.getParent();
        }

        // Notify the interpreter's listeners.
        Message message = new Message(
                MessageType.RUNTIME_ERROR,
                new Object[]{
                        errorCode.toString(),
                        node.getAttribute(ICodeKeyEnumImpl.LINE)
                }
        );
        backend.sendMessage(message);

        if (++errorCount > MAX_ERRORS) {
            System.out.println("*** ABORTED AFTER TOO MANY RUNTIME ERRORS.");
            System.exit(-1);
        }
    }
}
