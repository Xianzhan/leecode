package xianzhan.pascal.backend.compiler.generator;

import xianzhan.pascal.backend.compiler.CodeGenerator;
import xianzhan.pascal.backend.compiler.Directive;
import xianzhan.pascal.backend.compiler.PascalCompilerException;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

/**
 * Generate code for a statement.
 *
 * @author xianzhan
 * @since 2019-07-20
 */
public class StatementGenerator extends CodeGenerator {

    /**
     * Constructor.
     *
     * @param parent parent executor.
     */
    public StatementGenerator(CodeGenerator parent) {
        super(parent);
    }

    /**
     * Generate code for a statement.
     * To be overridden by the specialized statement executor subclasses.
     *
     * @param node the root node of the statement.
     */
    @Override
    public void generate(ICodeNode node) throws PascalCompilerException {
        ICodeNodeTypeEnumImpl nodeType = (ICodeNodeTypeEnumImpl) node.getType();
        int line = 0;

        if (nodeType != ICodeNodeTypeEnumImpl.COMPOUND) {
            line = getLineNumber(node);
            emitDirective(Directive.LINE, line);
        }

        // Generate code for a statement according to the type of statement.
        switch (nodeType) {

            case COMPOUND: {
                CompoundGenerator compoundGenerator = new CompoundGenerator(this);
                compoundGenerator.generate(node);
                break;
            }

            case ASSIGN: {
                AssignmentGenerator assignmentGenerator = new AssignmentGenerator(this);
                assignmentGenerator.generate(node);
                break;
            }
            default:
        }

        // Verify that the stack height after each statement is 0.
        if (localStack.getSize() != 0) {
            throw new PascalCompilerException(String.format("Stack size error: size = %d after line %d", localStack.getSize(), line));
        }
    }

    /**
     * Get the source line number of a parse tree node.
     *
     * @param node the parse tree node.
     * @return the line number.
     */
    private int getLineNumber(ICodeNode node) {
        Object lineNumber = null;

        // Go up the parent links to look for a line number.
        while ((node != null) && ((lineNumber = node.getAttribute(ICodeKeyEnumImpl.LINE)) == null)) {
            node = node.getParent();
        }

        return (Integer) lineNumber;
    }
}
