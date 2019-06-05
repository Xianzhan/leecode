package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageType;

import java.util.ArrayList;

/**
 * Execute an assignment statement.
 *
 * @author xianzhan
 * @since 2019-06-05
 */
public class AssignmentExecutor extends StatementExecutor {

    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public AssignmentExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute an assignment statement.
     *
     * @param node the root node of the statement.
     * @return null.
     */
    @Override
    public Object execute(ICodeNode node) {
        // The ASSIGN node's children are the target variable
        // and the expression.
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode variableNode = children.get(0);
        ICodeNode expressionNode = children.get(1);

        // Execute the expression and get it's value.
        ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
        Object value = expressionExecutor.execute(expressionNode);

        // Set the value as an attribute of the variable's symbol table entry.
        SymTabEntry variableId = (SymTabEntry) variableNode.getAttribute(ICodeKeyEnumImpl.ID);
        variableId.setAttribute(SymTabKeyImpl.DATA_VALUE, value);

        sendMessage(node, variableId.getName(), value);

        ++executionCount;
        return null;
    }

    /**
     * Send a message about the assignment operation.
     *
     * @param node         the ASSIGN node.
     * @param variableName the name of the target variable.
     * @param value        the value of the expression.
     */
    private void sendMessage(ICodeNode node,
                             String variableName,
                             Object value) {
        Object lineNumber = node.getAttribute(ICodeKeyEnumImpl.LINE);

        // Send an ASSIGN message.
        if (lineNumber != null) {
            Message message = new Message(
                    MessageType.ASSIGN,
                    new Object[]{
                            lineNumber,
                            variableName,
                            value
                    }
            );
            sendMessage(message);
        }
    }
}
