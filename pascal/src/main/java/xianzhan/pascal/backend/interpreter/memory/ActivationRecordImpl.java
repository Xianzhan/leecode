package xianzhan.pascal.backend.interpreter.memory;

import xianzhan.pascal.backend.interpreter.ActivationRecord;
import xianzhan.pascal.backend.interpreter.Cell;
import xianzhan.pascal.backend.interpreter.MemoryFactory;
import xianzhan.pascal.backend.interpreter.MemoryMap;
import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;

import java.util.ArrayList;

/**
 * The runtime activation record.
 *
 * @author xianzhan
 * @since 2019-07-02
 */
public class ActivationRecordImpl implements ActivationRecord {

    /**
     * symbol table entry of the routine's name
     */
    private SymTabEntry      routineId;
    /**
     * dynamic link to the previous record
     */
    private ActivationRecord link;
    /**
     * scope nesting level of this record
     */
    private int              nestingLevel;
    /**
     * memory map of this stack record
     */
    private MemoryMap        memoryMap;

    /**
     * Constructor.
     *
     * @param routineId the symbol table entry of the routine's name.
     */
    public ActivationRecordImpl(SymTabEntry routineId) {
        SymTab symTab = (SymTab) routineId.getAttribute(SymTabKeyImpl.ROUTINE_SYMTAB);

        this.routineId = routineId;
        this.nestingLevel = symTab.getNestingLevel();
        this.memoryMap = MemoryFactory.createMemoryMap(symTab);
    }

    /**
     * Getter.
     *
     * @return the symbol table entry of the routine's name.
     */
    @Override
    public SymTabEntry getRoutineId() {
        return routineId;
    }

    /**
     * Return the memory cell for the given name from the memory map.
     *
     * @param name the name.
     * @return the cell.
     */
    @Override
    public Cell getCell(String name) {
        return memoryMap.getCell(name);
    }

    /**
     * Return the list of all the names in the memory map.
     *
     * @return the list of all the names in the memory map.
     */
    @Override
    public ArrayList<String> getAllNames() {
        return memoryMap.getAllNames();
    }

    /**
     * Getter.
     *
     * @return the scope nesting level.
     */
    @Override
    public int getNestingLevel() {
        return nestingLevel;
    }

    /**
     * Return the activation record to which this record is dynamically linked.
     *
     * @return the activation record to which this record is dynamically linked.
     */
    @Override
    public ActivationRecord linkedTo() {
        return link;
    }

    /**
     * Make a dynamic link from this activation record to another one.
     *
     * @param ar the activation record to link to.
     * @return this activation record.
     */
    @Override
    public ActivationRecord makeLinkTo(ActivationRecord ar) {
        link = ar;
        return this;
    }
}
