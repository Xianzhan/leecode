package xianzhan.pascal.backend.interpreter;

import java.util.ArrayList;

/**
 * Interface for the interpreter's runtime memory map.
 *
 * @author xianzhan
 * @since 2019-07-03
 */
public interface MemoryMap {

    /**
     * Return the memory cell with the given name.
     *
     * @param name the name.
     * @return the cell.
     */
    Cell getCell(String name);

    /**
     * Return the list of all the names.
     *
     * @return the list of all the names.
     */
    ArrayList<String> getAllNames();
}
