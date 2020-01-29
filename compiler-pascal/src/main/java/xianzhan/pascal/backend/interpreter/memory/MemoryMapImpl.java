package xianzhan.pascal.backend.interpreter.memory;

import xianzhan.pascal.backend.interpreter.Cell;
import xianzhan.pascal.backend.interpreter.MemoryFactory;
import xianzhan.pascal.backend.interpreter.MemoryMap;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeForm;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The interpreter's runtime memory map.
 *
 * @author xianzhan
 * @since 2019-07-03
 */
public class MemoryMapImpl extends HashMap<String, Cell> implements MemoryMap {

    /**
     * Constructor.
     * Create a memory map and allocate its memory cells
     * based on the entries in a symbol table.
     *
     * @param symTab the symbol table.
     */
    public MemoryMapImpl(SymTab symTab) {
        ArrayList<SymTabEntry> entries = symTab.sortedEntries();

        // Loop for each entry of the symbol table.
        entries.forEach(entry -> {
            Definition definition = entry.getDefinition();

            // Not a VAR parameter: Allocate cells for the data type
            //                      in the hash map.
            if (definition == DefinitionEnumImpl.VARIABLE
                    || definition == DefinitionEnumImpl.FUNCTION
                    || definition == DefinitionEnumImpl.VALUE_PARM
                    || definition == DefinitionEnumImpl.FIELD) {
                String name = entry.getName();
                TypeSpec type = entry.getTypeSpec();
                put(name, MemoryFactory.createCell(allocateCellValue(type)));
            } else if (definition == DefinitionEnumImpl.VAR_PARM) {
                // VAR parameter: Allocate a single cell to hold a reference
                //                in the hash map.
                String name = entry.getName();
                put(name, MemoryFactory.createCell(null));
            }
        });
    }

    /**
     * Return the memory cell with the given name.
     *
     * @param name the name.
     * @return the cell.
     */
    @Override
    public Cell getCell(String name) {
        return get(name);
    }

    /**
     * Return the list of all the names.
     *
     * @return the list of all the names.
     */
    @Override
    public ArrayList<String> getAllNames() {
        return new ArrayList<>(keySet());
    }

    /**
     * Make an allocation for a value of a given data type for a memory cell.
     *
     * @param type the data type.
     * @return the allocation.
     */
    private Object allocateCellValue(TypeSpec type) {
        TypeForm form = type.getForm();

        switch ((TypeFormEnumImpl) form) {

            case ARRAY: {
                return allocateArrayCells(type);
            }

            case RECORD: {
                return allocateRecordMap(type);
            }

            default: {
                // uninitialized scalar value
                return null;
            }
        }
    }

    /**
     * Allocate the memory cells of an array.
     *
     * @param type the array type.
     * @return the allocation.
     */
    private Object[] allocateArrayCells(TypeSpec type) {
        int elementCount = (int) type.getAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_COUNT);
        TypeSpec elementType = (TypeSpec) type.getAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_TYPE);
        Cell[] allocation = new Cell[elementCount];

        for (int i = 0; i < elementCount; i++) {
            allocation[i] = MemoryFactory.createCell(allocateCellValue(elementType));
        }

        return allocation;
    }

    /**
     * Allocate the memory map for a record.
     *
     * @param type the record type.
     * @return the allocation.
     */
    private MemoryMap allocateRecordMap(TypeSpec type) {
        SymTab symTab = (SymTab) type.getAttribute(TypeKeyEnumImpl.RECORD_SYMTAB);
        return MemoryFactory.createMemoryMap(symTab);
    }
}
