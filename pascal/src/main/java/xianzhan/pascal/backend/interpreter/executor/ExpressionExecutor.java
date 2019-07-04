package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.BackendFactory;
import xianzhan.pascal.backend.interpreter.ActivationRecord;
import xianzhan.pascal.backend.interpreter.Cell;
import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.backend.interpreter.RuntimeErrorCode;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.ICodeNodeType;
import xianzhan.pascal.intermediate.RoutineCode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.RoutineCodeEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Execute an expression.
 *
 * @author xianzhan
 * @since 2019-06-05
 */
public class ExpressionExecutor extends StatementExecutor {

    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public ExpressionExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute an expression.
     *
     * @param node the root node of the statement.
     * @return the computed value of the expression.
     */
    @Override
    public Object execute(ICodeNode node) {
        ICodeNodeTypeEnumImpl nodeType = (ICodeNodeTypeEnumImpl) node.getType();
        switch (nodeType) {
            case VARIABLE: {
                // Return the variable's value.
                return executeValue(node);
            }
            case INTEGER_CONSTANT:
                TypeSpec type = node.getTypeSpec();
                Integer value = (Integer) node.getAttribute(ICodeKeyEnumImpl.VALUE);

                // If boolean, return true or false.
                // Else return the integer value.
                return type == Predefined.booleanType
                        // true or false
                        ? value == 1
                        // integer value
                        : value;
            case REAL_CONSTANT:
                // Return the float value.
            case STRING_CONSTANT:
                // Return the string value.
                return node.getAttribute(ICodeKeyEnumImpl.VALUE);
            case NEGATE: {
                // Get the NEGATE node's expression node child.
                ArrayList<ICodeNode> children = node.getChildren();
                ICodeNode expressionNode = children.get(0);

                // Execute the expression and return the negative of it's value.
                Object ret = execute(expressionNode);
                if (ret instanceof Integer) {
                    return -((Integer) ret);
                } else {
                    return -((Float) ret);
                }
            }
            case NOT: {
                // Get the NOT node's expression node child.
                ArrayList<ICodeNode> children = node.getChildren();
                ICodeNode expressionNode = children.get(0);

                // Execute the expression and return the "not" of it's value.
                Boolean ret = (Boolean) execute(expressionNode);
                return !ret;
            }
            case CALL: {
                // Execute a function call.
                SymTabEntry functionId = (SymTabEntry) node.getAttribute(ICodeKeyEnumImpl.ID);
                RoutineCode routineCode = (RoutineCode) functionId.getAttribute(SymTabKeyImpl.ROUTINE_CODE);
                CallExecutor callExecutor = new CallExecutor(this);
                Object ret = callExecutor.execute(node);

                // If it was a declared function, obtain the function value
                // from its name.
                if (routineCode == RoutineCodeEnumImpl.DECLARED) {
                    String functionName = functionId.getName();
                    int nestingLevel = functionId.getSymTab().getNestingLevel();
                    ActivationRecord ar = runtimeStack.getTopmost(nestingLevel);
                    Cell functionValueCell = ar.getCell(functionName);
                    ret = functionValueCell.getValue();

                    sendFetchMessage(node, functionId.getName(), ret);
                }

                // Return the function value.
                return ret;
            }

            // Must be a binary operator.
            default:
                return executeBinaryOperator(node, nodeType);
        }
    }

    /**
     * Return a variable's value.
     *
     * @param node ICodeNode
     * @return Object
     */
    private Object executeValue(ICodeNode node) {
        SymTabEntry variableId = (SymTabEntry) node.getAttribute(ICodeKeyEnumImpl.ID);
        String variableName = variableId.getName();
        TypeSpec variableType = variableId.getTypeSpec();

        // Get the variable's value.
        Cell variableCell = executeVariable(node);
        Object value = variableCell.getValue();

        if (value != null) {
            value = toJava(variableType, value);
        }

        // Uninitialized value error: Use a default value.
        else {
            errorHandler.flag(node, RuntimeErrorCode.UNINITIALIZED_VALUE, this);

            value = BackendFactory.defaultValue(variableType);
            variableCell.setValue(value);
        }

        sendFetchMessage(node, variableName, value);
        return value;
    }

    /**
     * Execute a variable and return the reference to its cell.
     *
     * @param node the variable node.
     * @return the reference to the variable's cell.
     */
    public Cell executeVariable(ICodeNode node) {
        SymTabEntry variableId = (SymTabEntry) node.getAttribute(ICodeKeyEnumImpl.ID);
        String variableName = variableId.getName();
        TypeSpec variableType = variableId.getTypeSpec();
        int nestingLevel = variableId.getSymTab().getNestingLevel();

        // Get the variable reference from the appropriate activation record.
        ActivationRecord ar = runtimeStack.getTopmost(nestingLevel);
        Cell variableCell = ar.getCell(variableName);

        ArrayList<ICodeNode> modifiers = node.getChildren();

        // Reference to a reference: Use the original reference.
        if (variableCell.getValue() instanceof Cell) {
            variableCell = (Cell) variableCell.getValue();
        }

        // Execute any array subscripts or record fields.
        for (ICodeNode modifier : modifiers) {
            ICodeNodeType nodeType = modifier.getType();

            // Subscripts.
            if (nodeType == ICodeNodeTypeEnumImpl.SUBSCRIPTS) {
                ArrayList<ICodeNode> subscripts = modifier.getChildren();

                // Compute a new reference for each subscript.
                for (ICodeNode subscript : subscripts) {
                    TypeSpec indexType = (TypeSpec) variableType.getAttribute(TypeKeyEnumImpl.ARRAY_INDEX_TYPE);
                    int minIndex = indexType.getForm() == TypeFormEnumImpl.SUBRANGE
                            ? (Integer) indexType.getAttribute(TypeKeyEnumImpl.SUBRANGE_MIN_VALUE)
                            : 0;

                    int value = (Integer) execute(subscript);
                    value = (Integer) checkRange(node, indexType, value);

                    int index = value - minIndex;
                    variableCell = ((Cell[]) variableCell.getValue())[index];
                    variableType = (TypeSpec) variableType.getAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_TYPE);
                }
            }

            // Field.
            else if (nodeType == ICodeNodeTypeEnumImpl.FIELD) {
                SymTabEntry fieldId = (SymTabEntry) modifier.getAttribute(ICodeKeyEnumImpl.ID);
                String fieldName = fieldId.getName();

                // Compute a new reference for the field.
                HashMap<String, Cell> map = (HashMap<String, Cell>) variableCell.getValue();
                variableCell = map.get(fieldName);
                variableType = fieldId.getTypeSpec();
            }
        }

        return variableCell;
    }

    /**
     * Set of arithmetic operator node types.
     */
    private static final EnumSet<ICodeNodeTypeEnumImpl> ARITH_OPS =
            EnumSet.of(
                    ICodeNodeTypeEnumImpl.ADD,
                    ICodeNodeTypeEnumImpl.SUBTRACT,
                    ICodeNodeTypeEnumImpl.MULTIPLY,
                    ICodeNodeTypeEnumImpl.FLOAT_DIVIDE,
                    ICodeNodeTypeEnumImpl.INTEGER_DIVIDE,
                    ICodeNodeTypeEnumImpl.MOD
            );

    /**
     * Execute a binary operator.
     *
     * @param node     the root node of the expression.
     * @param nodeType the node type.
     * @return the computed value of the expression.
     */
    private Object executeBinaryOperator(ICodeNode node, ICodeNodeTypeEnumImpl nodeType) {
        // Get the two operand children of the operator node.
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode operandNode1 = children.get(0);
        ICodeNode operandNode2 = children.get(1);

        // Operands.
        Object operand1 = execute(operandNode1);
        Object operand2 = execute(operandNode2);

        boolean integerMode = (operand1 instanceof Integer) && (operand2 instanceof Integer);
        boolean characterMode = (operand1 instanceof Character || (operand1 instanceof String && ((String) operand1).length() == 1)
                && (operand2 instanceof Character || (operand2 instanceof String && ((String) operand2).length() == 1)));
        boolean stringMode = operand1 instanceof String && operand2 instanceof String;

        // ====================
        // Arithmetic operators
        // ====================

        if (ARITH_OPS.contains(nodeType)) {
            if (integerMode) {
                int value1 = (int) operand1;
                int value2 = (int) operand2;

                // Integer operations.
                switch (nodeType) {
                    case ADD:
                        return value1 + value2;
                    case SUBTRACT:
                        return value1 - value2;
                    case MULTIPLY:
                        return value1 * value2;
                    case FLOAT_DIVIDE: {
                        // Check for division by zero.
                        if (value2 != 0) {
                            return ((float) value1) / ((float) value2);
                        } else {
                            errorHandler.flag(node, RuntimeErrorCode.DIVISION_BY_ZERO, this);
                            return 0;
                        }
                    }
                    case INTEGER_DIVIDE: {
                        // Check for division by zero.
                        if (value2 != 0) {
                            return value1 / value2;
                        } else {
                            errorHandler.flag(node, RuntimeErrorCode.DIVISION_BY_ZERO, this);
                            return 0;
                        }
                    }
                    case MOD: {
                        // Check for division by zero.
                        if (value2 != 0) {
                            return value1 % value2;
                        } else {
                            errorHandler.flag(node, RuntimeErrorCode.DIVISION_BY_ZERO, this);
                            return 0;
                        }
                    }
                    default: // do nothing
                }
            } else {
                float value1 = operand1 instanceof Integer
                        ? (int) operand1
                        : (float) operand1;
                float value2 = operand2 instanceof Integer
                        ? (int) operand2
                        : (float) operand2;
                // Float operations.
                switch (nodeType) {
                    case ADD:
                        return value1 + value2;
                    case SUBTRACT:
                        return value1 - value2;
                    case MULTIPLY:
                        return value1 * value2;
                    case FLOAT_DIVIDE: {
                        // Check for division by zero.
                        if (value2 != 0.0F) {
                            return value1 / value2;
                        } else {
                            errorHandler.flag(
                                    node,
                                    RuntimeErrorCode.DIVISION_BY_ZERO,
                                    this
                            );
                            return 0.0F;
                        }
                    }
                    default: // do nothing
                }
            }
        }
        // ==========
        // AND and OR
        // ==========

        else if ((nodeType == ICodeNodeTypeEnumImpl.AND) || (nodeType == ICodeNodeTypeEnumImpl.OR)) {
            boolean value1 = (boolean) operand1;
            boolean value2 = (boolean) operand2;

            switch (nodeType) {
                case AND:
                    return value1 && value2;
                case OR:
                    return value1 || value2;
                default:// do nothing
            }
        }

        // ====================
        // Relational operators
        // ====================

        else if (integerMode) {
            int value1 = (int) operand1;
            int value2 = (int) operand2;

            // Integer operands.
            switch (nodeType) {
                case EQ:
                    return value1 == value2;
                case NE:
                    return value1 != value2;
                case LT:
                    return value1 < value2;
                case LE:
                    return value1 <= value2;
                case GT:
                    return value1 > value2;
                case GE:
                    return value1 >= value2;
                default: // do nothing
            }

        } else if (characterMode) {
            int value1 = operand1 instanceof Character
                    ? (Character) operand1
                    : ((String) operand1).charAt(0);
            int value2 = operand2 instanceof Character
                    ? (Character) operand2
                    : ((String) operand2).charAt(0);

            // Character operands.
            switch (nodeType) {
                case EQ:
                    return value1 == value2;
                case NE:
                    return value1 != value2;
                case LT:
                    return value1 < value2;
                case LE:
                    return value1 <= value2;
                case GT:
                    return value1 > value2;
                case GE:
                    return value1 >= value2;
                default:
            }
        } else if (stringMode) {
            String value1 = (String) operand1;
            String value2 = (String) operand2;

            // String operands.
            int comp = value1.compareTo(value2);
            switch (nodeType) {
                case EQ:
                    return comp == 0;
                case NE:
                    return comp != 0;
                case LT:
                    return comp < 0;
                case LE:
                    return comp <= 0;
                case GT:
                    return comp > 0;
                case GE:
                    return comp >= 0;
                default:
            }
        } else {
            float value1 = operand1 instanceof Integer
                    ? (int) operand1 :
                    (float) operand1;
            float value2 = operand2 instanceof Integer
                    ? (int) operand2
                    : (float) operand2;

            // Float operands.
            switch (nodeType) {
                case EQ:
                    return value1 == value2;
                case NE:
                    return value1 != value2;
                case LT:
                    return value1 < value2;
                case LE:
                    return value1 <= value2;
                case GT:
                    return value1 > value2;
                case GE:
                    return value1 >= value2;
                default: // do nothing
            }
        }

        // should never get here
        return 0;
    }
}
