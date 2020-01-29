package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.ActivationRecord;
import xianzhan.pascal.backend.interpreter.Cell;
import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.backend.interpreter.MemoryFactory;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.ICode;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;

import java.util.ArrayList;

/**
 * Execute a call a declared procedure or function.
 *
 * @author xianzhan
 * @since 2019-07-03
 */
public class CallDeclaredExecutor extends CallExecutor {
    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public CallDeclaredExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute a call to a declared procedure or function.
     *
     * @param node the CALL node.
     * @return null.
     */
    @Override
    public Object execute(ICodeNode node) {
        SymTabEntry routineId = (SymTabEntry) node.getAttribute(ICodeKeyEnumImpl.ID);
        ActivationRecord newAr = MemoryFactory.createActivationRecord(routineId);

        // Execute any actual parameters and initialize
        // the formal parameters in the new activation record.
        if (node.getChildren().size() > 0) {
            ICodeNode paramsNode = node.getChildren().get(0);
            ArrayList<ICodeNode> actualNodes = paramsNode.getChildren();
            ArrayList<SymTabEntry> formalIds = (ArrayList<SymTabEntry>) routineId.getAttribute(SymTabKeyImpl.ROUTINE_PARMS);
            executeActualParams(actualNodes, formalIds, newAr);
        }

        // Push the new activation record.
        runtimeStack.push(newAr);

        sendCallMessage(node, routineId.getName());

        // Get the root node of the routine's intermediate code.
        ICode iCode = (ICode) routineId.getAttribute(SymTabKeyImpl.ROUTINE_ICODE);
        ICodeNode rootNode = iCode.getRoot();

        // Execute the routine.
        StatementExecutor statementExecutor = new StatementExecutor(this);
        Object value = statementExecutor.execute(rootNode);

        // Pop off the activation record.
        runtimeStack.pop();

        sendReturnMessage(node, routineId.getName());
        return value;
    }

    /**
     * Execute the actual parameters of a call.
     *
     * @param actualNodes the list of nodes of the actual params.
     * @param formalIds   the list of symbol table entries of the formal params.
     * @param newAr       the new activation record.
     */
    private void executeActualParams(ArrayList<ICodeNode> actualNodes, ArrayList<SymTabEntry> formalIds, ActivationRecord newAr) {
        ExpressionExecutor expressionExecutor = new ExpressionExecutor(this);
        AssignmentExecutor assignmentExecutor = new AssignmentExecutor(this);

        for (int i = 0; i < formalIds.size(); ++i) {
            SymTabEntry formalId = formalIds.get(i);
            Definition formalDefinition = formalId.getDefinition();
            Cell formalCell = newAr.getCell(formalId.getName());
            ICodeNode actualNode = actualNodes.get(i);

            // Value parameter.
            if (formalDefinition == DefinitionEnumImpl.VALUE_PARM) {
                TypeSpec formalType = formalId.getTypeSpec();
                TypeSpec valueType = actualNode.getTypeSpec().baseType();
                Object value = expressionExecutor.execute(actualNode);

                assignmentExecutor.assignValue(actualNode, formalId, formalCell, formalType, value, valueType);
            }

            // VAR parameter.
            else {
                Cell actualCell = expressionExecutor.executeVariable(actualNode);
                formalCell.setValue(actualCell);
            }
        }
    }
}
