package xianzhan.pascal.backend.interpreter;

import xianzhan.pascal.backend.interpreter.memory.ActivationRecordImpl;
import xianzhan.pascal.backend.interpreter.memory.CellImpl;
import xianzhan.pascal.backend.interpreter.memory.MemoryMapImpl;
import xianzhan.pascal.backend.interpreter.memory.RuntimeDisplayImpl;
import xianzhan.pascal.backend.interpreter.memory.RuntimeStackImpl;
import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;

/**
 * A factory class that creates runtime components.
 *
 * @author xianzhan
 * @since 2019-07-02
 */
public class MemoryFactory {

    /**
     * Create a runtime stack.
     *
     * @return the new runtime stack.
     */
    public static RuntimeStack createRuntimeStack() {
        return new RuntimeStackImpl();
    }

    /**
     * Create a runtime display.
     *
     * @return the new runtime display.
     */
    public static RuntimeDisplay createRuntimeDisplay() {
        return new RuntimeDisplayImpl();
    }

    /**
     * Create an activation record for a routine.
     *
     * @param routineId the symbol table entry of the routine's name.
     * @return the new activation record.
     */
    public static ActivationRecord createActivationRecord(SymTabEntry routineId) {
        return new ActivationRecordImpl(routineId);
    }

    /**
     * Create a memory map from a symbol table.
     *
     * @param symTab a symbol table
     * @return the new memory map.
     */
    public static MemoryMap createMemoryMap(SymTab symTab) {
        return new MemoryMapImpl(symTab);
    }

    /**
     * Create a memory cell.
     *
     * @param value the value for the cell.
     * @return the new memory cell.
     */
    public static Cell createCell(Object value) {
        return new CellImpl(value);
    }
}
