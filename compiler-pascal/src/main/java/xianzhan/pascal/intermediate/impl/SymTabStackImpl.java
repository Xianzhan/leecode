package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.SymTabFactory;
import xianzhan.pascal.intermediate.SymTabStack;

import java.util.ArrayList;

/**
 * An implementation of the symbol table stack.
 * <p>
 * Implement the symbol table stack as an array list.
 *
 * @author xianzhan
 * @since 2019-05-20
 */
public class SymTabStackImpl extends ArrayList<SymTab> implements SymTabStack {

    /**
     * current scope nesting level.
     */
    private int         currentNestingLevel;
    /**
     * entry for the main program id.
     */
    private SymTabEntry programId;

    public SymTabStackImpl() {
        // The constructor creates and adds the first symbol table
        // to the stack (the global table).
        this.currentNestingLevel = 0;
        add(SymTabFactory.createSymTab(currentNestingLevel));
    }

    @Override
    public int getCurrentNestingLevel() {
        return currentNestingLevel;
    }

    @Override
    public SymTab getLocalSymTab() {
        return get(currentNestingLevel);
    }

    @Override
    public SymTabEntry enterLocal(String name) {
        return get(currentNestingLevel).enter(name);
    }

    @Override
    public SymTabEntry lookupLocal(String name) {
        return get(currentNestingLevel).lookup(name);
    }

    /**
     * Look up an existing symbol table entry throughout the stack.
     *
     * @param name the name of the entry.
     * @return the entry, or null if it does not exist.
     */
    @Override
    public SymTabEntry lookup(String name) {
        SymTabEntry foundEntry = null;

        // Search the current and enclosing scopes.
        for (int i = currentNestingLevel; i >= 0 && foundEntry == null; --i) {
            foundEntry = get(i).lookup(name);
        }
        return foundEntry;
    }

    @Override
    public void setProgramId(SymTabEntry id) {
        this.programId = id;
    }

    @Override
    public SymTabEntry getProgramId() {
        return programId;
    }

    @Override
    public SymTab push() {
        SymTab symTab = SymTabFactory.createSymTab(++currentNestingLevel);
        add(symTab);
        return symTab;
    }

    @Override
    public SymTab push(SymTab symTab) {
        ++currentNestingLevel;
        add(symTab);
        return symTab;
    }

    @Override
    public SymTab pop() {
        SymTab symTab = get(currentNestingLevel);
        remove(currentNestingLevel--);
        return symTab;
    }
}
