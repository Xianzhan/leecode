package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.Definition;

/**
 * How a Pascal symbol table entry is defined.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public enum DefinitionEnumImpl implements Definition {
    //
    CONSTANT,
    ENUMERATION_CONSTANT("enumeration constant"),
    TYPE,
    VARIABLE,
    FIELD("record field"),
    VALUE_PARM("value parameter"),
    VAR_PARM("VAR parameter"),
    PROGRAM_PARM("program parameter"),
    PROGRAM,
    PROCEDURE,
    FUNCTION,
    UNDEFINED;

    private String text;

    /**
     * Constructor.
     */
    DefinitionEnumImpl() {
        this.text = this.toString().toLowerCase();
    }

    /**
     * Constructor.
     *
     * @param text the text for the definition code.
     */
    DefinitionEnumImpl(String text) {
        this.text = text;
    }

    /**
     * Getter.
     *
     * @return the text of the definition code.
     */
    @Override
    public String getText() {
        return text;
    }
}
