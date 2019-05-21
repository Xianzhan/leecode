package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.SymTabKey;

/**
 * Attribute keys for a symbol table entry.
 *
 * @author xianzhan
 * @since 2019-05-21
 */
public enum SymTabKeyImpl implements SymTabKey {
    /**
     * Constant.
     */
    CONSTANT_VALUE,

    /**
     * Procedure or function.
     */
    ROUTINE_CODE,
    ROUTINE_SYMTAB,
    ROUTINE_ICODE,
    ROUTINE_PARMS,
    ROUTINE_ROUTINES,

    /**
     * Variable or record field value.
     */
    DATA_VALUE
}
