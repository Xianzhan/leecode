package xianzhan.pascal.intermediate;

import java.util.ArrayList;

/**
 * <h1>SymTab</h1>
 *
 * <p>The framework interface that represents the symbol table.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 *
 * @author Ronald Mak
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
     * @return a list of symbol table entries sorted by name.
     */
    ArrayList<SymTabEntry> sortedEntries();
}
