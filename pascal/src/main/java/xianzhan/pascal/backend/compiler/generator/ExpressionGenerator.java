package xianzhan.pascal.backend.compiler.generator;

import xianzhan.pascal.backend.compiler.CodeGenerator;
import xianzhan.pascal.backend.compiler.Instruction;
import xianzhan.pascal.backend.compiler.Label;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.TypeChecker;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Generate code for an expression.
 *
 * @author xianzhan
 * @since 2019-07-21
 */
public class ExpressionGenerator extends StatementGenerator {
    /**
     * Constructor.
     *
     * @param parent parent executor.
     */
    public ExpressionGenerator(CodeGenerator parent) {
        super(parent);
    }

    /**
     * Generate code to evaluate an expression.
     *
     * @param node the root intermediate code node of the compound statement.
     */
    @Override
    public void generate(ICodeNode node) {
        ICodeNodeTypeEnumImpl nodeType = (ICodeNodeTypeEnumImpl) node.getType();

        switch (nodeType) {

            case VARIABLE: {

                // Generate code to load a variable's value.
                generateLoadValue(node);
                break;
            }

            case INTEGER_CONSTANT: {
                TypeSpec type = node.getTypeSpec();
                Integer value = (Integer) node.getAttribute(ICodeKeyEnumImpl.VALUE);

                // Generate code to load a boolean constant
                // 0 (false) or 1 (true).
                if (type == Predefined.booleanType) {
                    emitLoadConstant(value == 1 ? 1 : 0);
                }

                // Generate code to load an integer constant.
                else {
                    emitLoadConstant(value);
                }

                localStack.increase(1);
                break;
            }

            case REAL_CONSTANT: {
                float value = (Float) node.getAttribute(ICodeKeyEnumImpl.VALUE);

                // Generate code to load a float constant.
                emitLoadConstant(value);

                localStack.increase(1);
                break;
            }

            case STRING_CONSTANT: {
                String value = (String) node.getAttribute(ICodeKeyEnumImpl.VALUE);

                // Generate code to load a string constant.
                if (node.getTypeSpec() == Predefined.charType) {
                    emitLoadConstant(value.charAt(0));
                } else {
                    emitLoadConstant(value);
                }

                localStack.increase(1);
                break;
            }

            case NEGATE: {

                // Get the NEGATE node's expression node child.
                ArrayList<ICodeNode> children = node.getChildren();
                ICodeNode expressionNode = children.get(0);

                // Generate code to evaluate the expression and
                // negate its value.
                generate(expressionNode);
                emit(expressionNode.getTypeSpec() == Predefined.integerType
                        ? Instruction.INEG : Instruction.FNEG);

                break;
            }

            case NOT: {

                // Get the NOT node's expression node child.
                ArrayList<ICodeNode> children = node.getChildren();
                ICodeNode expressionNode = children.get(0);

                // Generate code to evaluate the expression and NOT its value.
                generate(expressionNode);
                emit(Instruction.ICONST_1);
                emit(Instruction.IXOR);

                localStack.use(1);
                break;
            }

            // Must be a binary operator.
            default:
                generateBinaryOperator(node, nodeType);
        }
    }

    /**
     * Generate code to load a variable's value.
     *
     * @param variableNode the variable node.
     */
    protected void generateLoadValue(ICodeNode variableNode) {
        generateLoadVariable(variableNode);
    }

    /**
     * Generate code to load a variable's address (structured) or
     * value (scalar).
     *
     * @param variableNode the variable node.
     */
    protected TypeSpec generateLoadVariable(ICodeNode variableNode) {
        SymTabEntry variableId = (SymTabEntry) variableNode.getAttribute(ICodeKeyEnumImpl.ID);
        TypeSpec variableType = variableId.getTypeSpec();

        emitLoadVariable(variableId);
        localStack.increase(1);

        return variableType;
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
     * Generate code to evaluate a binary operator.
     *
     * @param node     the root node of the expression.
     * @param nodeType the node type.
     */
    private void generateBinaryOperator(ICodeNode node, ICodeNodeTypeEnumImpl nodeType) {
        // Get the two operand children of the operator node.
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode operandNode1 = children.get(0);
        ICodeNode operandNode2 = children.get(1);
        TypeSpec type1 = operandNode1.getTypeSpec();
        TypeSpec type2 = operandNode2.getTypeSpec();

        boolean integerMode = TypeChecker.areBothInteger(type1, type2)
                || (type1.getForm() == TypeFormEnumImpl.ENUMERATION)
                || (type2.getForm() == TypeFormEnumImpl.ENUMERATION);
        boolean realMode = TypeChecker.isAtLeastOneReal(type1, type2)
                || (nodeType == ICodeNodeTypeEnumImpl.FLOAT_DIVIDE);
        boolean characterMode = TypeChecker.isChar(type1)
                && TypeChecker.isChar(type2);
        boolean stringMode = type1.isPascalString()
                && type2.isPascalString();

        if (!stringMode) {
            // Emit code to evaluate the first operand.
            generate(operandNode1);
            if (realMode && TypeChecker.isInteger(type1)) {
                emit(Instruction.I2F);
            }

            // Emit code to evaluate the second operand.
            generate(operandNode2);
            if (realMode && TypeChecker.isInteger(type2)) {
                emit(Instruction.I2F);
            }
        }

        // ====================
        // Arithmetic operators
        // ====================

        if (ARITH_OPS.contains(nodeType)) {
            if (integerMode) {

                // Integer operations.
                switch (nodeType) {
                    case ADD:
                        emit(Instruction.IADD);
                        break;
                    case SUBTRACT:
                        emit(Instruction.ISUB);
                        break;
                    case MULTIPLY:
                        emit(Instruction.IMUL);
                        break;
                    case FLOAT_DIVIDE:
                        emit(Instruction.FDIV);
                        break;
                    case INTEGER_DIVIDE:
                        emit(Instruction.IDIV);
                        break;
                    case MOD:
                        emit(Instruction.IREM);
                        break;
                    default:
                }
            } else {

                // Float operations.
                switch (nodeType) {
                    case ADD:
                        emit(Instruction.FADD);
                        break;
                    case SUBTRACT:
                        emit(Instruction.FSUB);
                        break;
                    case MULTIPLY:
                        emit(Instruction.FMUL);
                        break;
                    case FLOAT_DIVIDE:
                        emit(Instruction.FDIV);
                        break;
                    default:
                }
            }

            localStack.decrease(1);
        }

        // ==========
        // AND and OR
        // ==========

        else if (nodeType == ICodeNodeTypeEnumImpl.AND) {
            emit(Instruction.IAND);
            localStack.decrease(1);
        } else if (nodeType == ICodeNodeTypeEnumImpl.OR) {
            emit(Instruction.IOR);
            localStack.decrease(1);
        }

        // ====================
        // Relational operators
        // ====================

        else {
            Label trueLabel = Label.newLabel();
            Label nextLabel = Label.newLabel();

            if (integerMode || characterMode) {
                switch (nodeType) {
                    case EQ:
                        emit(Instruction.IF_ICMPEQ, trueLabel);
                        break;
                    case NE:
                        emit(Instruction.IF_ICMPNE, trueLabel);
                        break;
                    case LT:
                        emit(Instruction.IF_ICMPLT, trueLabel);
                        break;
                    case LE:
                        emit(Instruction.IF_ICMPLE, trueLabel);
                        break;
                    case GT:
                        emit(Instruction.IF_ICMPGT, trueLabel);
                        break;
                    case GE:
                        emit(Instruction.IF_ICMPGE, trueLabel);
                        break;
                    default:
                }

                localStack.decrease(2);
            } else if (realMode) {
                emit(Instruction.FCMPG);

                switch (nodeType) {
                    case EQ:
                        emit(Instruction.IFEQ, trueLabel);
                        break;
                    case NE:
                        emit(Instruction.IFNE, trueLabel);
                        break;
                    case LT:
                        emit(Instruction.IFLT, trueLabel);
                        break;
                    case LE:
                        emit(Instruction.IFLE, trueLabel);
                        break;
                    case GT:
                        emit(Instruction.IFGT, trueLabel);
                        break;
                    case GE:
                        emit(Instruction.IFGE, trueLabel);
                        break;
                    default:
                }

                localStack.decrease(2);
            }

            // false
            emit(Instruction.ICONST_0);
            emit(Instruction.GOTO, nextLabel);
            emitLabel(trueLabel);
            // true
            emit(Instruction.ICONST_1);
            emitLabel(nextLabel);

            localStack.increase(1);
        }
    }
}
