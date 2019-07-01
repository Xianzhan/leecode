package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.backend.interpreter.RuntimeErrorCode;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageType;

/**
 * Execute a statement.
 *
 * @author xianzhan
 * @since 2019-06-05
 */
public class StatementExecutor extends Executor {

    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public StatementExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute a statement.
     * To be overridden by the specialized statement executor subclasses.
     *
     * @param node the root node of the statement.
     * @return null.
     */
    public Object execute(ICodeNode node) {
        ICodeNodeTypeEnumImpl nodeType = (ICodeNodeTypeEnumImpl) node.getType();

        // Send a message about the current source line.
        sendSourceLineMessage(node);

        switch (nodeType) {
            case COMPOUND: {
                CompoundExecutor compoundExecutor = new CompoundExecutor(this);
                return compoundExecutor.execute(node);
            }

            case ASSIGN: {
                AssignmentExecutor assignmentExecutor = new AssignmentExecutor(this);
                return assignmentExecutor.execute(node);
            }

            case LOOP: {
                LoopExecutor loopExecutor = new LoopExecutor(this);
                return loopExecutor.execute(node);
            }

            case IF: {
                IfExecutor ifExecutor = new IfExecutor(this);
                return ifExecutor.execute(node);
            }

            case SELECT: {
                SelectExecutor selectExecutor = new SelectExecutor(this);
                return selectExecutor.execute(node);
            }

            case NO_OP:
                return null;

            default: {
                errorHandler.flag(node, RuntimeErrorCode.UNIMPLEMENTED_FEATURE, this);
                return null;
            }
        }
    }

    private void sendSourceLineMessage(ICodeNode node) {
        Object lineNumber = node.getAttribute(ICodeKeyEnumImpl.LINE);

        // Send the SOURCE_LINE message.
        if (lineNumber != null) {
            sendMessage(new Message(MessageType.SOURCE_LINE, lineNumber));
        }
    }
}
