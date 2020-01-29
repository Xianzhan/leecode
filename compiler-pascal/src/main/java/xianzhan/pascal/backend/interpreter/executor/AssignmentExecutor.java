package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Cell;
import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;

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
        Cell targetCell = expressionExecutor.executeVariable(variableNode);
        TypeSpec targetType = variableNode.getTypeSpec();
        TypeSpec valueType = expressionNode.getTypeSpec().baseType();
        Object value = expressionExecutor.execute(expressionNode);

        // Set the value as an attribute of the variable's symbol table entry.
        SymTabEntry variableId = (SymTabEntry) variableNode.getAttribute(ICodeKeyEnumImpl.ID);
        variableId.setAttribute(SymTabKeyImpl.DATA_VALUE, value);

        assignValue(node, variableId, targetCell, targetType, value, valueType);

        ++executionCount;
        return null;
    }

    /**
     * Assign a value to a target cell.
     *
     * @param node       the ancester parse tree node of the assignment.
     * @param targetId   the symbol table entry of the target variable or parm.
     * @param targetCell the target cell.
     * @param targetType the target type.
     * @param value      the value to assign.
     * @param valueType  the value type.
     */
    protected void assignValue(ICodeNode node, SymTabEntry targetId, Cell targetCell, TypeSpec targetType, Object value, TypeSpec valueType) {
        // Range check.
        value = checkRange(node, targetType, value);

        // Set the target's value.
        // Convert an integer value to real if necessary.
        if ((targetType == Predefined.realType) && (valueType == Predefined.integerType)) {
            targetCell.setValue(((Number) value).floatValue());
        }

        // String assignment:
        //   target length < value length: truncate the value
        //   target length > value length: blank pad the value
        else if (targetType != null && targetType.isPascalString()) {
            int targetLength = (Integer) targetType.getAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_COUNT);
            int valueLength = (Integer) valueType.getAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_COUNT);
            String stringValue = (String) value;

            // Truncate the value string.
            if (targetLength < valueLength) {
                stringValue = stringValue.substring(0, targetLength);
            }

            // Pad the value string with blanks at the right end.
            else if (targetLength > valueLength) {
                stringValue = stringValue + " ".repeat(Math.max(0, targetLength - valueLength));
            }

            targetCell.setValue(copyOf(toPascal(targetType, stringValue), node));
        }

        // Simple assignment.
        else {
            targetCell.setValue(copyOf(toPascal(targetType, value), node));
        }

        sendAssignMessage(node, targetId.getName(), value);
    }
}
