package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Execute a SELECT statement. Optimized.
 *
 * @author xianzhan
 * @since 2019-06-19
 */
public class SelectExecutor extends StatementExecutor {

    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public SelectExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Jump table cache: entry key is a SELECT node, entry value is the jump table.
     * <p>
     * Jump table: entry key is a selection value, entry value is the branch statement.
     */
    private static HashMap<ICodeNode, HashMap<Object, ICodeNode>> jumpCache = new HashMap<>();

    /**
     * Execute SELECT statement.
     * <p>
     * Method  execute() executes the  SELECT node’s first child, which is the expression that evaluates to the selection value that
     * searchBranches() uses to search the node’s  SELECT_BRANCH children. If  searchBranches() returns a reference to a branch, then method
     * execute() executes that branch’s statement.
     * <p>
     * This version of class  SelectExecutor is not very efficient. Method  searchBranches() performs a linear search of the
     * SELECT_BRANCH subtrees, and  searchConstants() performs a linear search of each branch’s constants. Performance becomes slower if
     * the source statement has more branches and constants. It takes longer to find a matching branch that appears later in the statement
     * than one that appears earlier.
     *
     * @param node the root node of the statement.
     * @return null.
     */
    @Override
    public Object execute(ICodeNode node) {
        // Is there already an entry for this SELECT node in the
        // jump table cache? If not, create a jump table entry.
        HashMap<Object, ICodeNode> jumpTable = jumpCache.get(node);
        if (jumpTable == null) {
            jumpTable = createJumpTable(node);
            jumpCache.put(node, jumpTable);
        }

        // Get the SELECT node's children.
        ArrayList<ICodeNode> selectChildren = node.getChildren();
        ICodeNode exprNode = selectChildren.get(0);

        // Evaluate the SELECT expression.
        ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
        Object selectValue = expressionExecutor.execute(exprNode);

        // If there is a selection, execute the SELECT_BRANCH's statement.
        ICodeNode statementNode = jumpTable.get(selectValue);
        if (statementNode != null) {
            StatementExecutor statementExecutor = new StatementExecutor(this);
            statementExecutor.execute(statementNode);
        }

        // count the SELECT statement itself.
        ++executionCount;
        return null;
    }

    /**
     * Create a jump table for a SELECT node.
     *
     * @param node the SELECT node.
     * @return the jump table.
     */
    private HashMap<Object, ICodeNode> createJumpTable(ICodeNode node) {
        HashMap<Object, ICodeNode> jumpTable = new HashMap<>();

        // Loop over children that area SELECT_BRANCH node.
        ArrayList<ICodeNode> selectChildren = node.getChildren();
        for (int i = 1; i < selectChildren.size(); i++) {
            ICodeNode branchNode = selectChildren.get(i);
            ICodeNode constantsNode = branchNode.getChildren().get(0);
            ICodeNode statementNode = branchNode.getChildren().get(1);

            // Loop over the constants children of the branch's CONSTANTS_NODE.
            ArrayList<ICodeNode> constantsList = constantsNode.getChildren();
            for (ICodeNode constantNode : constantsList) {

                // Create a jump table entry.
                Object value = constantNode.getAttribute(ICodeKeyEnumImpl.VALUE);
                jumpTable.put(value, statementNode);
            }
        }

        return jumpTable;
    }
}
