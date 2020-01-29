package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.SymTabFactory;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * An implementation of the symbol table.
 *
 * @author xianzhan
 * @since 2019-05-20
 */
public class SymTabImpl extends TreeMap<String, SymTabEntry> implements SymTab {

    /**
     * Scope nesting level of this entry
     */
    private int nestingLevel;
    /**
     * local variables array slot number
     */
    private int slotNumber;
    /**
     * max slot number value
     */
    private int maxSlotNumber;

    public SymTabImpl(int nestingLevel) {
        this.nestingLevel = nestingLevel;
        this.slotNumber = -1;
        this.maxSlotNumber = 0;
    }

    /**
     * Create and enter a new entry into the symbol table.
     *
     * @param name the name of the entry.
     * @return the new entry.
     */
    @Override
    public SymTabEntry enter(String name) {
        SymTabEntry entry = SymTabFactory.createSymTabEntry(name, this);
        put(name, entry);

        return entry;
    }

    /**
     * Look up an existing symbol table entry.
     *
     * @param name the name of the entry.
     * @return the entry, or null if it does not exist.
     */
    @Override
    public SymTabEntry lookup(String name) {
        return get(name);
    }

    /**
     * @return a list of symbol table entries sorted by name.
     */
    @Override
    public ArrayList<SymTabEntry> sortedEntries() {
        return new ArrayList<>(values());
    }

    /**
     * @return the scope nesting level of this entry.
     */
    @Override
    public int getNestingLevel() {
        return nestingLevel;
    }


    /**
     * @return the next local variables array slot number.
     */
    @Override
    public int nextSlotNumber() {
        maxSlotNumber = ++slotNumber;
        return slotNumber;
    }

    /**
     * @return the maximum local variables array slot number.
     */
    @Override
    public int maxSlotNumber() {
        return maxSlotNumber;
    }
}
