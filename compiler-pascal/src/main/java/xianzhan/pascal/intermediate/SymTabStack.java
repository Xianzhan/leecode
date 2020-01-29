package xianzhan.pascal.intermediate;

/**
 * Symbol table stack
 *
 * @author xianzhan
 * @since 2019-05-20
 */
public interface SymTabStack {

    /**
     * @return the current nesting level.
     */
    int getCurrentNestingLevel();

    /**
     * @return the local symbol table.
     */
    SymTab getLocalSymTab();

    /**
     * Create and enter a new entry into the local symbol table.
     *
     * @param name the name of the entry.
     * @return the new entry.
     */
    SymTabEntry enterLocal(String name);

    /**
     * Look up an existing symbol table entry in the local symbol table.
     *
     * @param name the name of the entry.
     * @return the entry, or null if it does not exist.
     */
    SymTabEntry lookupLocal(String name);

    /**
     * Look up an existing symbol table entry throughout the stack.
     *
     * @param name the name of the entry.
     * @return the entry, or null if it does not exist.
     */
    SymTabEntry lookup(String name);

    /**
     * Setter.
     *
     * @param entry the symbol table entry for the main program identifier.
     */
    void setProgramId(SymTabEntry entry);

    /**
     * Getter.
     *
     * @return the symbol table entry for the main program identifier.
     */
    SymTabEntry getProgramId();

    /**
     * Push a new symbol table onto the stack.
     *
     * @return the pushed symbol table.
     */
    SymTab push();


    /**
     * Push a symbol table onto the stack.
     *
     * @param symTab the symbol table to push.
     * @return the pushed symbol table.
     */
    SymTab push(SymTab symTab);


    /**
     * Pop a symbol table off the stack.
     *
     * @return the popped symbol table.
     */
    SymTab pop();
}
