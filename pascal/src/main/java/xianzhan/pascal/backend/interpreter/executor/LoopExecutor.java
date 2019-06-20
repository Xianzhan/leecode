package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

import java.util.ArrayList;

/**
 * Execute a loop statement.
 *
 * @author xianzhan
 * @since 2019-06-19
 */
public class LoopExecutor extends StatementExecutor {
    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public LoopExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute a loop statement.
     * <p>
     * The  execute() method’s  while loop repeatedly interprets the children of the  LOOP node. The inner  for loop executes each child that
     * is a statement subtree. One of the children can be a  TEST node whose child is a relative expression subtree. The method executes the
     * expression and exits the loop if the expression evaluates to true.
     *
     * @param node the root node of the statement.
     * @return null.
     */
    @Override
    public Object execute(ICodeNode node) {
        boolean exitLoop = false;
        ICodeNode exprNode = null;
        ArrayList<ICodeNode> loopChildren = node.getChildren();

        ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
        StatementExecutor statementExecutor = new StatementExecutor(this);

        // Loop util the TEST expression value is true.
        while (!exitLoop) {
            // count the loop statement itself.
            ++executionCount;

            // Execute the children of the LOOP node.
            for (ICodeNode child : loopChildren) {
                ICodeNodeTypeEnumImpl childType = (ICodeNodeTypeEnumImpl) child.getType();

                // TEST node?
                if (childType == ICodeNodeTypeEnumImpl.TEST) {
                    if (exprNode == null) {
                        exprNode = child.getChildren().get(0);
                    }
                    exitLoop = (boolean) expressionExecutor.execute(exprNode);
                } else {
                    // Statement node.
                    statementExecutor.execute(child);
                }

                // Exit if the TEST expression value is true.
                if (exitLoop) {
                    break;
                }
            }
        }

        return null;
    }
}
