package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.RoutineCode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.RoutineCodeEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;

/**
 * Execute a call to a procedure or function.
 *
 * @author xianzhan
 * @since 2019-07-03
 */
public class CallExecutor extends StatementExecutor {
    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public CallExecutor(Executor parent) {
        super(parent);
    }


    /**
     * Execute procedure or function call statement.
     *
     * @param node the root node of the call.
     * @return null.
     */
    @Override
    public Object execute(ICodeNode node) {
        SymTabEntry routineId = (SymTabEntry) node.getAttribute(ICodeKeyEnumImpl.ID);
        RoutineCode routineCode = (RoutineCode) routineId.getAttribute(SymTabKeyImpl.ROUTINE_CODE);
        CallExecutor callExecutor = routineCode == RoutineCodeEnumImpl.DECLARED
                ? new CallDeclaredExecutor(this)
                : new CallStandardExecutor(this);

        // Count the call statement.
        ++executionCount;
        return callExecutor.execute(node);
    }
}
