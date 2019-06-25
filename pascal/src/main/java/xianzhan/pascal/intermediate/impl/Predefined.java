package xianzhan.pascal.intermediate.impl;

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

    /**
     * Initialize a symbol table stack with predefined identifiers.
     *
     * @param symTabStack the symbol table stack to initialize.
     */
    public static void initialize(SymTabStack symTabStack) {
        initializeTypes(symTabStack);
        initializeConstants(symTabStack);
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
}
