package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.RoutineCode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.SymTabStack;
import xianzhan.pascal.intermediate.TypeFactory;
import xianzhan.pascal.intermediate.TypeSpec;

import java.util.ArrayList;

/**
 * Enter the predefined Pascal types, identifiers, and constants
 * into the symbol table.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public class Predefined {
    // Predefined types.

    public static TypeSpec integerType;
    public static TypeSpec realType;
    public static TypeSpec booleanType;
    public static TypeSpec charType;
    public static TypeSpec undefinedType;

    // Predefined identifiers.

    public static SymTabEntry integerId;
    public static SymTabEntry realId;
    public static SymTabEntry booleanId;
    public static SymTabEntry charId;
    public static SymTabEntry falseId;
    public static SymTabEntry trueId;

    public static SymTabEntry readId;
    public static SymTabEntry readlnId;
    public static SymTabEntry writeId;
    public static SymTabEntry writelnId;
    public static SymTabEntry absId;
    public static SymTabEntry arctanId;
    public static SymTabEntry chrId;
    public static SymTabEntry cosId;
    public static SymTabEntry eofId;
    public static SymTabEntry eolnId;
    public static SymTabEntry expId;
    public static SymTabEntry lnId;
    public static SymTabEntry oddId;
    public static SymTabEntry ordId;
    public static SymTabEntry predId;
    public static SymTabEntry roundId;
    public static SymTabEntry sinId;
    public static SymTabEntry sqrId;
    public static SymTabEntry sqrtId;
    public static SymTabEntry succId;
    public static SymTabEntry truncId;

    /**
     * Initialize a symbol table stack with predefined identifiers.
     *
     * @param symTabStack the symbol table stack to initialize.
     */
    public static void initialize(SymTabStack symTabStack) {
        initializeTypes(symTabStack);
        initializeConstants(symTabStack);
        initializeStandardRoutines(symTabStack);
    }

    /**
     * Initialize the predefined types.
     *
     * @param symTabStack the symbol table stack to initialize.
     */
    private static void initializeTypes(SymTabStack symTabStack) {
        // Type integer.
        integerId = symTabStack.enterLocal("integer");
        integerType = TypeFactory.createType(TypeFormEnumImpl.SCALAR);
        integerType.setIdentifier(integerId);
        integerId.setDefinition(DefinitionEnumImpl.TYPE);
        integerId.setTypeSpec(integerType);

        // Type real.
        realId = symTabStack.enterLocal("real");
        realType = TypeFactory.createType(TypeFormEnumImpl.SCALAR);
        realType.setIdentifier(realId);
        realId.setDefinition(DefinitionEnumImpl.TYPE);
        realId.setTypeSpec(realType);

        // Type boolean.
        booleanId = symTabStack.enterLocal("boolean");
        booleanType = TypeFactory.createType(TypeFormEnumImpl.ENUMERATION);
        booleanType.setIdentifier(booleanId);
        booleanId.setDefinition(DefinitionEnumImpl.TYPE);
        booleanId.setTypeSpec(booleanType);

        // Type char.
        charId = symTabStack.enterLocal("char");
        charType = TypeFactory.createType(TypeFormEnumImpl.SCALAR);
        charType.setIdentifier(charId);
        charId.setDefinition(DefinitionEnumImpl.TYPE);
        charId.setTypeSpec(charType);

        // Undefined type.
        undefinedType = TypeFactory.createType(TypeFormEnumImpl.SCALAR);
    }

    /**
     * Initialize the predefined constant.
     *
     * @param symTabStack the symbol table stack to initialize.
     */
    private static void initializeConstants(SymTabStack symTabStack) {
        // Boolean enumeration constant false.
        falseId = symTabStack.enterLocal("false");
        falseId.setDefinition(DefinitionEnumImpl.ENUMERATION_CONSTANT);
        falseId.setTypeSpec(booleanType);
        falseId.setAttribute(SymTabKeyImpl.CONSTANT_VALUE, 0);

        // Boolean enumeration constant true.
        trueId = symTabStack.enterLocal("true");
        trueId.setDefinition(DefinitionEnumImpl.ENUMERATION_CONSTANT);
        trueId.setTypeSpec(booleanType);
        trueId.setAttribute(SymTabKeyImpl.CONSTANT_VALUE, 1);

        // Add false and true to the boolean enumeration type.
        ArrayList<SymTabEntry> constants = new ArrayList<>();
        constants.add(falseId);
        constants.add(trueId);
        booleanType.setAttribute(TypeKeyEnumImpl.ENUMERATION_CONSTANTS, constants);
    }

    /**
     * Initialize the standard procedures and functions.
     *
     * @param symTabStack the symbol table stack to initialize.
     */
    private static void initializeStandardRoutines(SymTabStack symTabStack) {
        readId = enterStandard(symTabStack, DefinitionEnumImpl.PROCEDURE, "read", RoutineCodeEnumImpl.READ);
        readlnId = enterStandard(symTabStack, DefinitionEnumImpl.PROCEDURE, "readln", RoutineCodeEnumImpl.READLN);
        writeId = enterStandard(symTabStack, DefinitionEnumImpl.PROCEDURE, "write", RoutineCodeEnumImpl.WRITE);
        writelnId = enterStandard(symTabStack, DefinitionEnumImpl.PROCEDURE, "writeln", RoutineCodeEnumImpl.WRITELN);

        absId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "abs", RoutineCodeEnumImpl.ABS);
        arctanId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "arctan", RoutineCodeEnumImpl.ARCTAN);
        chrId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "chr", RoutineCodeEnumImpl.CHR);
        cosId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "cos", RoutineCodeEnumImpl.COS);
        eofId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "eof", RoutineCodeEnumImpl.EOF);
        eolnId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "eoln", RoutineCodeEnumImpl.EOLN);
        expId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "exp", RoutineCodeEnumImpl.EXP);
        lnId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "ln", RoutineCodeEnumImpl.LN);
        oddId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "odd", RoutineCodeEnumImpl.ODD);
        ordId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "ord", RoutineCodeEnumImpl.ORD);
        predId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "pred", RoutineCodeEnumImpl.PRED);
        roundId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "round", RoutineCodeEnumImpl.ROUND);
        sinId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "sin", RoutineCodeEnumImpl.SIN);
        sqrId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "sqr", RoutineCodeEnumImpl.SQR);
        sqrtId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "sqrt", RoutineCodeEnumImpl.SQRT);
        succId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "succ", RoutineCodeEnumImpl.SUCC);
        truncId = enterStandard(symTabStack, DefinitionEnumImpl.FUNCTION, "trunc", RoutineCodeEnumImpl.TRUNC);
    }

    /**
     * Enter a standard procedure or function into the symbol table stack.
     *
     * @param symTabStack the symbol table stack to initialize.
     * @param definition  either PROCEDURE or FUNCTION.
     * @param name        the procedure or function name.
     */
    private static SymTabEntry enterStandard(SymTabStack symTabStack, Definition definition, String name, RoutineCode routineCode) {
        SymTabEntry procId = symTabStack.enterLocal(name);
        procId.setDefinition(definition);
        procId.setAttribute(SymTabKeyImpl.ROUTINE_CODE, routineCode);

        return procId;
    }
}
