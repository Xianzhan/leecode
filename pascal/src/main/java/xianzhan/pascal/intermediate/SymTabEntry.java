package xianzhan.pascal.intermediate;

import java.util.ArrayList;

/**
 * Symbol table entry.
 *
 * @author xianzhan
 * @since 2019-05-20
 */
public interface SymTabEntry {

    /**
     * Return the name of the entry.
     *
     * @return the name of the entry.
     */
    String getName();

    /**
     * Return the symbol table that contains this entry.
     *
     * @return the symbol table that contains this entry.
     */
    SymTab getSymTab();

    /**
     * Append a source line number to the entry.
     *
     * @param lineNumber the line number to append.
     */
    void appendLineNumber(int lineNumber);

    /**
     * Return the list of source line numbers.
     *
     * @return the list of source line numbers.
     */
    ArrayList<Integer> getLineNumbers();

    /**
     * Set an attribute of the entry.
     *
     * @param key   the attribute key.
     * @param value the attribute value.
     */
    void setAttribute(SymTabKey key, Object value);

    /**
     * Get the value of an attribute of the entry.
     *
     * @param key the attribute key.
     * @return the attribute value.
     */
    Object getAttribute(SymTabKey key);

    /**
     * Setter.
     *
     * @param definition the definition to set.
     */
    void setDefinition(Definition definition);

    /**
     * Getter.
     *
     * @return the definition.
     */
    Definition getDefinition();

    /**
     * Setter.
     *
     * @param typeSpec the type specification to set.
     */
    void setTypeSpec(TypeSpec typeSpec);

    /**
     * Getter.
     *
     * @return the type specification.
     */
    TypeSpec getTypeSpec();
}
