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
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;

import java.util.ArrayList;

/**
 * Generate code for a declared procedure or function.
 *
 * @author xianzhan
 * @since 2019-07-21
 */
public class DeclaredRoutineGenerator extends CodeGenerator {

    private SymTabEntry routineId;
    private String      routineName;

    /**
     * Function return value slot number.
     */
    private int functionValueSlot;

    public DeclaredRoutineGenerator(CodeGenerator parent) {
        super(parent);
    }

    /**
     * Generate code for a declared procedure or function
     *
     * @param routineId the symbol table entry of the routine's name.
     */
    @Override
    public void generate(SymTabEntry routineId) throws PascalCompilerException {
        this.routineId = routineId;
        this.routineName = routineId.getName();

        SymTab routineSymTab = (SymTab) routineId.getAttribute(SymTabKeyImpl.ROUTINE_SYMTAB);
        localVariables = new LocalVariables(routineSymTab.maxSlotNumber());
        localStack = new LocalStack();

        // Reserve an extra variable for the function return value.
        if (routineId.getDefinition() == DefinitionEnumImpl.FUNCTION) {
            functionValueSlot = localVariables.reserve();
            routineId.setAttribute(SymTabKeyImpl.SLOT, functionValueSlot);
        }

        generateRoutineHeader();
        generateRoutineLocals();

        // Generate code to allocate any arrays, records, and strings.
        StructuredDataGenerator structuredDataGenerator = new StructuredDataGenerator(this);
        structuredDataGenerator.generate(routineId);

        generateRoutineCode();
        generateRoutineReturn();
        generateRoutineEpilogue();
    }

    /**
     * Generate the routine header.
     */
    private void generateRoutineHeader() {
        String routineName = routineId.getName();
        ArrayList<SymTabEntry> paramIds = (ArrayList<SymTabEntry>) routineId.getAttribute(SymTabKeyImpl.ROUTINE_PARMS);
        StringBuilder buffer = new StringBuilder();

        // Procedure or function name.
        buffer.append(routineName);
        buffer.append("(");

        // Parameter and return type descriptors.
        if (paramIds != null) {
            for (SymTabEntry paramId : paramIds) {
                buffer.append(typeDescriptor(paramId));
            }
        }
        buffer.append(")");
        buffer.append(typeDescriptor(routineId));

        emitBlankLine();
        emitDirective(Directive.METHOD_PRIVATE_STATIC, buffer.toString());
    }

    /**
     * Generate directives for the local variables.
     */
    private void generateRoutineLocals() {
        SymTab symTab = (SymTab) routineId.getAttribute(SymTabKeyImpl.ROUTINE_SYMTAB);
        ArrayList<SymTabEntry> ids = symTab.sortedEntries();

        emitBlankLine();

        // Loop over all the routine's identifiers and
        // emit a .var directive for each variable and formal parameter.
        for (SymTabEntry id : ids) {
            Definition definition = id.getDefinition();

            if ((definition == DefinitionEnumImpl.VARIABLE)
                    || (definition == DefinitionEnumImpl.VALUE_PARM)
                    || (definition == DefinitionEnumImpl.VAR_PARM)) {
                int slot = (Integer) id.getAttribute(SymTabKeyImpl.SLOT);
                emitDirective(Directive.VAR, slot + " is " + id.getName(), typeDescriptor(id));
            }
        }

        // Emit an extra .var directive for an implied function variable.
        if (routineId.getDefinition() == DefinitionEnumImpl.FUNCTION) {
            emitDirective(Directive.VAR, functionValueSlot + " is " + routineName, typeDescriptor(routineId.getTypeSpec()));
        }
    }

    /**
     * Generate code for the routine's body.
     */
    private void generateRoutineCode() throws PascalCompilerException {
        ICode iCode = (ICode) routineId.getAttribute(SymTabKeyImpl.ROUTINE_ICODE);
        ICodeNode root = iCode.getRoot();

        emitBlankLine();

        // Generate code for the compound statement.
        StatementGenerator statementGenerator = new StatementGenerator(this);
        statementGenerator.generate(root);
    }

    /**
     * Generate the routine's return code.
     */
    private void generateRoutineReturn() {
        emitBlankLine();

        // Function: Return the value in the implied function variable.
        if (routineId.getDefinition() == DefinitionEnumImpl.FUNCTION) {
            TypeSpec type = routineId.getTypeSpec();

            emitLoadLocal(type, functionValueSlot);
            emitReturnValue(type);

            localStack.use(1);
        }

        // Procedure: Just return.
        else {
            emit(Instruction.RETURN);
        }
    }

    /**
     * Generate the routine's epilogue.
     */
    private void generateRoutineEpilogue() {
        emitBlankLine();
        emitDirective(Directive.LIMIT_LOCALS, localVariables.count());
        emitDirective(Directive.LIMIT_STACK, localStack.capacity());
        emitDirective(Directive.END_METHOD);
    }
}
