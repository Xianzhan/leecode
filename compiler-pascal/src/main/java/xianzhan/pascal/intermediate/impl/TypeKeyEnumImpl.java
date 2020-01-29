package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.TypeKey;

/**
 * Attribute keys for a Pascal type specification.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public enum TypeKeyEnumImpl implements TypeKey {

    // Enumeration
    ENUMERATION_CONSTANTS,

    // Subrange
    SUBRANGE_BASE_TYPE,
    SUBRANGE_MIN_VALUE,
    SUBRANGE_MAX_VALUE,

    // Array
    ARRAY_INDEX_TYPE,
    ARRAY_ELEMENT_TYPE,
    ARRAY_ELEMENT_COUNT,

    // Record
    RECORD_SYMTAB
}
