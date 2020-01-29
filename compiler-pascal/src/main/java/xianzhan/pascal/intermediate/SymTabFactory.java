package xianzhan.pascal.intermediate;

import xianzhan.pascal.intermediate.impl.SymTabEntryImpl;
import xianzhan.pascal.intermediate.impl.SymTabImpl;
import xianzhan.pascal.intermediate.impl.SymTabStackImpl;

/**
 * A factory for creating objects that implement the symbol table.
 *
 * @author xianzhan
 * @since 2019-05-20
 */
public class SymTabFactory {

    /**
     * Create and return a symbol table stack implementation.
     *
     * @return the symbol table implementation.
     */
    public static SymTabStack createSymTabStack() {
        return new SymTabStackImpl();
    }

    /**
     * Create and return a symbol table implementation.
     *
     * @param nestingLevel the nesting level.
     * @return the symbol table implementation.
     */
    public static SymTab createSymTab(int nestingLevel) {
        return new SymTabImpl(nestingLevel);
    }

    /**
     * Create and return a symbol table entry implementation.
     *
     * @param name   the identifier name.
     * @param symTab the symbol table that contains this entry.
     * @return the symbol table entry implementation.
     */
    public static SymTabEntry createSymTabEntry(String name, SymTab symTab) {
        return new SymTabEntryImpl(name, symTab);
    }
}
