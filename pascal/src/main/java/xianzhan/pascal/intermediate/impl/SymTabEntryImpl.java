package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.SymTabKey;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xianzhan
 * @since 2019-05-20
 */
public class SymTabEntryImpl extends HashMap<SymTabKey, Object> implements SymTabEntry {

    /**
     * entry name.
     */
    private String             name;
    /**
     * parent symbol table.
     */
    private SymTab             symTab;
    /**
     * source line numbers.
     */
    private ArrayList<Integer> lineNumbers;

    /**
     * @param name   the name of the entry.
     * @param symTab the symbol table that contains this entry.
     */
    public SymTabEntryImpl(String name, SymTab symTab) {
        this.name = name;
        this.symTab = symTab;
        this.lineNumbers = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SymTab getSymTab() {
        return symTab;
    }

    @Override
    public void appendLineNumber(int lineNumber) {
        lineNumbers.add(lineNumber);
    }

    @Override
    public ArrayList<Integer> getLineNumbers() {
        return lineNumbers;
    }

    @Override
    public void setAttribute(SymTabKey key, Object value) {
        put(key, value);
    }

    @Override
    public Object getAttribute(SymTabKey key) {
        return get(key);
    }
}
