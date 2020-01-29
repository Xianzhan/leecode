package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.intermediate.ICodeNode;

import java.util.ArrayList;

/**
 * Execute an IF statement.
 *
 * @author xianzhan
 * @since 2019-06-19
 */
public class IfExecutor extends StatementExecutor {

    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public IfExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute an IF statement.
     * <p>
     * The  execute() method executes the  IF node’s first child, which is a relative expression subtree. If the expression evaluates to true,
     * then the method executes the second child, the  THEN statement subtree. If the expression evaluates to false and there is a third child,
     * the method executes instead the third child, the  ELSE statement subtree.
     *
     * @param node the root node of the statement.
     * @return null.
     */
    @Override
    public Object execute(ICodeNode node) {
        // Get the IF node's children.
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode exprNode = children.get(0);
        ICodeNode thenStmtNode = children.get(1);
        ICodeNode elseStmtNode = children.size() > 2 ? children.get(2) : null;

        ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
        StatementExecutor statementExecutor = new StatementExecutor(this);

        // Evaluate the expression to determine which statement to execute.
        boolean expressionIsTrue = (boolean) expressionExecutor.execute(exprNode);
        if (expressionIsTrue) {
            statementExecutor.execute(thenStmtNode);
        } else if (elseStmtNode != null) {
            statementExecutor.execute(elseStmtNode);
        }

        // count the IF statement itself.
        ++executionCount;
        return null;
    }
}
