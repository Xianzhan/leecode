package xianzhan.pascal.intermediate;

import java.util.ArrayList;

/**
 * The framework interface that represents the symbol table.
 *
 * @author xianzhan
 * @since 2019-05-08
 */
public interface SymTab {

    /**
     * Return the scope nesting level of this entry.
     *
     * @return the scope nesting level of this entry.
     */
    int getNestingLevel();

    /**
     * Create and enter a new entry into the symbol table.
     *
     * @param name the name of the entry.
     * @return the new entry.
     */
    SymTabEntry enter(String name);

    /**
     * Look up an existing symbol table entry.
     *
     * @param name the name of the entry.
     * @return the entry, or null if it does not exist.
     */
    SymTabEntry lookup(String name);

    /**
     * return a list of symbol table entries sorted by name.
     *
     * @return a list of symbol table entries sorted by name.
     */
    ArrayList<SymTabEntry> sortedEntries();
}
