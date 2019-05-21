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
     * @return the name of the entry.
     */
    String getName();

    /**
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
}
