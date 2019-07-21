package xianzhan.pascal.backend.compiler.generator;

import xianzhan.pascal.backend.compiler.CodeGenerator;
import xianzhan.pascal.backend.compiler.Instruction;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;

import java.util.ArrayList;

import static xianzhan.pascal.intermediate.impl.SymTabKeyImpl.SLOT;

/**
 * Generate code for an assignment statement.
 *
 * @author xianzhan
 * @since 2019-07-20
 */
public class AssignmentGenerator extends StatementGenerator {

    /**
     * Constructor.
     *
     * @param parent parent executor.
     */
    public AssignmentGenerator(CodeGenerator parent) {
        super(parent);
    }

    /**
     * Generate code for an assignment statement.
     *
     * @param node the root node of the statement.
     */
    @Override
    public void generate(ICodeNode node) {
        TypeSpec assignmentType = node.getTypeSpec();

        // The ASSIGN node's children are the target variable
        // and the expression.
        ArrayList<ICodeNode> assignChildren = node.getChildren();
        ICodeNode targetNode = assignChildren.get(0);
        ICodeNode exprNode = assignChildren.get(1);

        SymTabEntry targetId = (SymTabEntry) targetNode.getAttribute(ICodeKeyEnumImpl.ID);
        TypeSpec targetType = targetNode.getTypeSpec();
        TypeSpec exprType = exprNode.getTypeSpec();
        ExpressionGenerator exprGenerator = new ExpressionGenerator(this);

        int slot;          // local variables array slot number of the target
        int nestingLevel;  // nesting level of the target
        SymTab symTab;     // symbol table that contains the target id

        // Assign a function value. Use the slot number of the function value.
        if (targetId.getDefinition() == DefinitionEnumImpl.FUNCTION) {
            slot = (Integer) targetId.getAttribute(SLOT);
            nestingLevel = 2;
        }

        // Standard assignment.
        else {
            symTab = targetId.getSymTab();
            slot = (Integer) targetId.getAttribute(SLOT);
            nestingLevel = symTab.getNestingLevel();
        }

        // Generate code to do the assignment.
        generateScalarAssignment(targetType, targetId,
                slot, nestingLevel, exprNode, exprType,
                exprGenerator);
    }

    /**
     * Generate code to assign a scalar value.
     *
     * @param targetType    the data type of the target.
     * @param targetId      the symbol table entry of the target variable.
     * @param index         the index of the target variable.
     * @param nestingLevel  the nesting level of the target variable.
     * @param exprNode      the expression tree node.
     * @param exprType      the expression data type.
     * @param exprGenerator the expression generator.
     */
    private void generateScalarAssignment(TypeSpec targetType,
                                          SymTabEntry targetId,
                                          int index, int nestingLevel,
                                          ICodeNode exprNode,
                                          TypeSpec exprType,
                                          ExpressionGenerator exprGenerator) {
        // Generate code to evaluate the expression.
        // Special cases: float variable := integer constant
        //                float variable := integer expression
        //                char variable  := single-character string constant
        if (targetType == Predefined.realType) {
            if (exprNode.getType() == ICodeNodeTypeEnumImpl.INTEGER_CONSTANT) {
                int value = (Integer) exprNode.getAttribute(ICodeKeyEnumImpl.VALUE);
                emitLoadConstant((float) value);
                localStack.increase(1);
            } else {
                exprGenerator.generate(exprNode);

                if (exprType.baseType() == Predefined.integerType) {
                    emit(Instruction.I2F);
                }
            }
        } else if ((targetType == Predefined.charType)
                && (exprNode.getType() == ICodeNodeTypeEnumImpl.STRING_CONSTANT)) {
            int value = ((String) exprNode.getAttribute(ICodeKeyEnumImpl.VALUE)).charAt(0);
            emitLoadConstant(value);
            localStack.increase(1);
        } else {
            exprGenerator.generate(exprNode);
        }

        // Generate code to store the expression value into the target variable.
        emitStoreVariable(targetId, nestingLevel, index);
        localStack.decrease(isWrapped(targetId) ? 2 : 1);
    }
}
