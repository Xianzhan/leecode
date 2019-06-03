package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.ICodeNodeType;

/**
 * Node types of the intermediate code parse tree.
 *
 * @author xianzhan
 * @since 2019-06-03
 */
public enum ICodeNodeTypeEnumImpl implements ICodeNodeType {

    // Program structure
    PROGRAM, PROCEDURE, FUNCTION,

    // Statements
    COMPOUND, ASSIGN, LOOP, TEST, CALL, PARAMETERS,
    IF, SELECT, SELECT_BRANCH, SELECT_CONSTANTS, NO_OP,

    // Relational operators
    EQ, NE, LT, LE, GT, GE, NOT,

    // Additive operators
    ADD, SUBTRACT, OR, NEGATE,

    // Multiplicative operators
    MULTIPLY, INTEGER_DIVIDE, FLOAT_DIVIDE, MOD, AND,

    // Operands
    VARIABLE, SUBSCRIPTS, FIELD,
    INTEGER_CONSTANT, REAL_CONSTANT,
    STRING_CONSTANT, BOOLEAN_CONSTANT,

    // WRITE parameter
    WRITE_PARM
}
