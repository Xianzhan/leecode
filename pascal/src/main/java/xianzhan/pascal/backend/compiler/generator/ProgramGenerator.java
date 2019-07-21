package xianzhan.pascal.backend.compiler.generator;

import xianzhan.pascal.backend.compiler.CodeGenerator;
import xianzhan.pascal.backend.compiler.Directive;
import xianzhan.pascal.backend.compiler.Instruction;
import xianzhan.pascal.backend.compiler.LocalStack;
import xianzhan.pascal.backend.compiler.LocalVariables;
import xianzhan.pascal.backend.compiler.PascalCompilerException;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.ICode;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;

import java.util.ArrayList;

/**
 * Generate code for the main program.
 *
 * @author xianzhan
 * @since 2019-07-20
 */
public class ProgramGenerator extends CodeGenerator {

    private SymTabEntry programId;
    private String      programName;

    public ProgramGenerator(CodeGenerator parent) {
        super(parent);
    }

    /**
     * Generate code for the main program.
     *
     * @param node the root node of the program.
     */
    @Override
    public void generate(ICodeNode node) throws PascalCompilerException {
        SymTabEntry programId = symTabStack.getProgramId();

        this.programId = programId;
        this.programName = programId.getName();

        localVariables = new LocalVariables(0);
        localStack = new LocalStack();

        emitDirective(Directive.CLASS_PUBLIC, programName);
        emitDirective(Directive.SUPER, "java/lang/Object");

        generateFields();
        generateConstructor();
        generateRoutines();
        generateMainMethod();
    }

    /**
     * Generate directives for the fields.
     */
    private void generateFields() {
        // Runtime timer and standard in.
        emitBlankLine();
        emitDirective(Directive.FIELD_PRIVATE_STATIC, "_runTimer", "LRunTimer;");
        emitDirective(Directive.FIELD_PRIVATE_STATIC, "_standardIn", "LPascalTextIn;");

        SymTab symTab = (SymTab) programId.getAttribute(SymTabKeyImpl.ROUTINE_SYMTAB);
        ArrayList<SymTabEntry> ids = symTab.sortedEntries();

        emitBlankLine();

        // Loop over all the program's identifiers and
        // emit a .field directive for each variable.
        for (SymTabEntry id : ids) {
            Definition definition = id.getDefinition();

            if (definition == DefinitionEnumImpl.VARIABLE) {
                emitDirective(Directive.FIELD_PRIVATE_STATIC, id.getName(), typeDescriptor(id));
            }
        }
    }

    /**
     * Generate code for the main program constructor.
     */
    private void generateConstructor() {
        emitBlankLine();
        emitDirective(Directive.METHOD_PUBLIC, "<init>()V");

        emitBlankLine();
        emit(Instruction.ALOAD_0);
        emit(Instruction.INVOKENONVIRTUAL, "java/lang/Object/<init>()V");
        emit(Instruction.RETURN);

        emitBlankLine();
        emitDirective(Directive.LIMIT_LOCALS, 1);
        emitDirective(Directive.LIMIT_STACK, 1);

        emitDirective(Directive.END_METHOD);
    }

    /**
     * Generate code for any nested procedures and functions.
     */
    private void generateRoutines() throws PascalCompilerException {
        DeclaredRoutineGenerator declaredRoutineGenerator = new DeclaredRoutineGenerator(this);
        ArrayList<SymTabEntry> routineIds = (ArrayList<SymTabEntry>) programId.getAttribute(SymTabKeyImpl.ROUTINE_ROUTINES);

        // Generate code for each procedure or function.
        for (SymTabEntry id : routineIds) {
            declaredRoutineGenerator.generate(id);
        }
    }

    /**
     * Generate code for the program body as the main method.
     */
    private void generateMainMethod()
            throws PascalCompilerException {
        emitBlankLine();
        emitDirective(Directive.METHOD_PUBLIC_STATIC, "main([Ljava/lang/String;)V");

        generateMainMethodPrologue();

        // Generate code to allocate any arrays, records, and strings.
        StructuredDataGenerator structuredDataGenerator = new StructuredDataGenerator(this);
        structuredDataGenerator.generate(programId);

        generateMainMethodCode();
        generateMainMethodEpilogue();
    }

    /**
     * Generate the main method prologue.
     */
    private void generateMainMethodPrologue() {
        String programName = programId.getName();

        // Runtime timer.
        emitBlankLine();
        emit(Instruction.NEW, "RunTimer");
        emit(Instruction.DUP);
        emit(Instruction.INVOKENONVIRTUAL, "RunTimer/<init>()V");
        emit(Instruction.PUTSTATIC, programName + "/_runTimer", "LRunTimer;");

        // Standard in.
        emit(Instruction.NEW, "PascalTextIn");
        emit(Instruction.DUP);
        emit(Instruction.INVOKENONVIRTUAL, "PascalTextIn/<init>()V");
        emit(Instruction.PUTSTATIC, programName + "/_standardIn LPascalTextIn;");

        localStack.use(3);
    }

    /**
     * Generate code for the main method.
     */
    private void generateMainMethodCode() throws PascalCompilerException {
        ICode iCode = (ICode) programId.getAttribute(SymTabKeyImpl.ROUTINE_ICODE);
        ICodeNode root = iCode.getRoot();

        emitBlankLine();

        // Generate code for the compound statement.
        StatementGenerator statementGenerator = new StatementGenerator(this);
        statementGenerator.generate(root);
    }

    /**
     * Generate the main method epilogue.
     */
    private void generateMainMethodEpilogue() {
        // Print the execution time.
        emitBlankLine();
        emit(Instruction.GETSTATIC, programName + "/_runTimer", "LRunTimer;");
        emit(Instruction.INVOKEVIRTUAL, "RunTimer.printElapsedTime()V");

        localStack.use(1);

        emitBlankLine();
        emit(Instruction.RETURN);
        emitBlankLine();

        emitDirective(Directive.LIMIT_LOCALS, localVariables.count());
        emitDirective(Directive.LIMIT_STACK, localStack.capacity());
        emitDirective(Directive.END_METHOD);
    }
}
