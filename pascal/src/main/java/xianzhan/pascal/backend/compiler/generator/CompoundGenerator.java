package xianzhan.pascal.backend.compiler.generator;

import xianzhan.pascal.backend.compiler.CodeGenerator;
import xianzhan.pascal.backend.compiler.Instruction;
import xianzhan.pascal.backend.compiler.PascalCompilerException;
import xianzhan.pascal.intermediate.ICodeNode;

import java.util.ArrayList;

/**
 * Generate code for a compound statement.
 *
 * @author xianzhan
 * @since 2019-07-21
 */
public class CompoundGenerator extends StatementGenerator {
    /**
     * Constructor.
     *
     * @param parent parent executor.
     */
    public CompoundGenerator(CodeGenerator parent) {
        super(parent);
    }

    /**
     * Generate code for a compound statement.
     *
     * @param node the root node of the compound statement.
     */
    @Override
    public void generate(ICodeNode node) throws PascalCompilerException {
        ArrayList<ICodeNode> children = node.getChildren();

        // Loop over the statement children of the COMPOUND node and generate
        // code for each statement. Emit a NOP is there are no statements.
        if (children.size() == 0) {
            emit(Instruction.NOP);
        } else {
            StatementGenerator statementGenerator = new StatementGenerator(this);

            for (ICodeNode child : children) {
                statementGenerator.generate(child);
            }
        }
    }
}
