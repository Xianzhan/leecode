package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.TypeForm;

/**
 * Type forms for a Pascal type specification.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public enum TypeFormEnumImpl implements TypeForm {
    /**
     * None
     */
    SCALAR,
    /**
     * List of enumeration constant identifiers
     */
    ENUMERATION,
    /**
     * Base type
     * Minimum value
     * Maximum value
     */
    SUBRANGE,
    /**
     * Index type
     * Minimum index value
     * Maximum index value
     * Element type
     * Element count
     */
    ARRAY,
    /**
     * A separate symbol table for the field identifiers
     */
    RECORD;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
