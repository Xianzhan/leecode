package xianzhan.pascal.backend.compiler.generator;

import xianzhan.pascal.backend.compiler.CodeGenerator;
import xianzhan.pascal.backend.compiler.PascalCompilerException;
import xianzhan.pascal.intermediate.SymTabEntry;

/**
 * Generate code to allocate arrays, records, and strings.
 *
 * @author xianzhan
 * @since 2019-07-21
 */
public class StructuredDataGenerator extends CodeGenerator {

    public StructuredDataGenerator(CodeGenerator parent) {
        super(parent);
    }

    /**
     * Generate code to allocate the structured data of a program,
     * procedure, or function.
     *
     * @param routineId the routine's symbol table entry.
     */
    @Override
    public void generate(SymTabEntry routineId) throws PascalCompilerException {

    }
}
