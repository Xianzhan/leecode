package xianzhan.pascal.backend.interpreter;

import xianzhan.pascal.intermediate.SymTabEntry;

import java.util.ArrayList;

/**
 * Interface for the interpreter's runtime activation record.
 *
 * @author xianzhan
 * @since 2019-07-02
 */
public interface ActivationRecord {

    /**
     * Getter.
     *
     * @return the symbol table entry of the routine's name.
     */
    SymTabEntry getRoutineId();

    /**
     * Return the memory cell for the given name from the memory map.
     *
     * @param name the name.
     * @return the cell.
     */
    Cell getCell(String name);

    /**
     * Return the list of all the names in the memory map.
     *
     * @return the list of all the names in the memory map.
     */
    ArrayList<String> getAllNames();

    /**
     * Getter.
     *
     * @return the scope nesting level.
     */
    int getNestingLevel();

    /**
     * Return the activation record to which this record is dynamically linked.
     *
     * @return the activation record to which this record is dynamically linked.
     */
    ActivationRecord linkedTo();

    /**
     * Make a dynamic link from this activation record to another one.
     *
     * @param ar the activation record to link to.
     * @return this activation record.
     */
    ActivationRecord makeLinkTo(ActivationRecord ar);
}
