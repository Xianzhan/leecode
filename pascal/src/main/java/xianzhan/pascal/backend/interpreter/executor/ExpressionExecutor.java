package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.backend.interpreter.RuntimeErrorCode;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;

import java.util.ArrayList;
import java.util.EnumSet;

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
                // Get the variable's symbol table entry and return it's value.
                SymTabEntry entry = (SymTabEntry) node.getAttribute(ICodeKeyEnumImpl.ID);
                return entry.getAttribute(SymTabKeyImpl.DATA_VALUE);
            }
            case INTEGER_CONSTANT: {
                // Return the integer value.
                return node.getAttribute(ICodeKeyEnumImpl.VALUE);
            }
            case REAL_CONSTANT: {
                // Return the float value.
                return node.getAttribute(ICodeKeyEnumImpl.VALUE);
            }
            case STRING_CONSTANT: {
                // Return the string value.
                return node.getAttribute(ICodeKeyEnumImpl.VALUE);
            }
            case NEGATE: {
                // Get the NEGATE node's expression node child.
                ArrayList<ICodeNode> children = node.getChildren();
                ICodeNode expressionNode = children.get(0);

                // Execute the expression and return the negative of it's value.
                Object value = execute(expressionNode);
                if (value instanceof Integer) {
                    return -((Integer) value);
                } else {
                    return -((Float) value);
                }
            }
            case NOT: {
                // Get the NOT node's expression node child.
                ArrayList<ICodeNode> children = node.getChildren();
                ICodeNode expressionNode = children.get(0);

                // Execute the expression and return the "not" of it's value.
                Boolean value = (Boolean) execute(expressionNode);
                return !value;
            }

            // Must be a binary operator.
            default:
                return executeBinaryOperator(node, nodeType);
        }
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
    private Object executeBinaryOperator(ICodeNode node,
                                         ICodeNodeTypeEnumImpl nodeType) {
        // Get the two operand children of the operator node.
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode operandNode1 = children.get(0);
        ICodeNode operandNode2 = children.get(1);

        // Operands.
        Object operand1 = execute(operandNode1);
        Object operand2 = execute(operandNode2);

        boolean integerMode =
                (operand1 instanceof Integer) && (operand2 instanceof Integer);

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
                            errorHandler.flag(
                                    node,
                                    RuntimeErrorCode.DIVISION_BY_ZERO,
                                    this
                            );
                            return 0;
                        }
                    }
                    case INTEGER_DIVIDE: {
                        // Check for division by zero.
                        if (value2 != 0) {
                            return value1 / value2;
                        } else {
                            errorHandler.flag(
                                    node,
                                    RuntimeErrorCode.DIVISION_BY_ZERO,
                                    this
                            );
                            return 0;
                        }
                    }
                    case MOD: {
                        // Check for division by zero.
                        if (value2 != 0) {
                            return value1 % value2;
                        } else {
                            errorHandler.flag(
                                    node,
                                    RuntimeErrorCode.DIVISION_BY_ZERO,
                                    this
                            );
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
        } else {
            float value1 = operand1 instanceof Integer
                    ? (int) operand1 : (float) operand1;
            float value2 = operand2 instanceof Integer
                    ? (int) operand2 : (float) operand2;

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
