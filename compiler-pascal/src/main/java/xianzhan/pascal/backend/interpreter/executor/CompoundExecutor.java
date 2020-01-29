package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.intermediate.ICodeNode;

import java.util.ArrayList;

/**
 * Execute a compound statement.
 *
 * @author xianzhan
 * @since 2019-06-05
 */
public class CompoundExecutor extends StatementExecutor {

    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public CompoundExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute a compound statement.
     *
     * @param node the root node of the statement.
     * @return null.
     */
    @Override
    public Object execute(ICodeNode node) {
        // Loop over the children of the COMPOUND node and execute each child.
        StatementExecutor statementExecutor = new StatementExecutor(this);
        ArrayList<ICodeNode> children = node.getChildren();
        for (ICodeNode child : children) {
            statementExecutor.execute(child);
        }

        return null;
    }
}
