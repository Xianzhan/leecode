package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.SymTabFactory;
import xianzhan.pascal.intermediate.SymTabStack;

import java.util.ArrayList;

/**
 * An implementation of the symbol table stack.
 *
 * @author xianzhan
 * @since 2019-05-20
 */
public class SymTabStackImpl extends ArrayList<SymTab> implements SymTabStack {

    /**
     * current scope nesting level.
     */
    private int currentNestingLevel;

    public SymTabStackImpl() {
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

    @Override
    public SymTabEntry lookup(String name) {
        return lookupLocal(name);
    }
}
