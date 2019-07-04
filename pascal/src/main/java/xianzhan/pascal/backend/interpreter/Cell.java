package xianzhan.pascal.backend.interpreter;

/**
 * Interface for the interpreter's runtime memory cell.
 *
 * @author xianzhan
 * @since 2019-07-02
 */
public interface Cell {

    /**
     * Set a new value into the cell.
     *
     * @param newValue the new value.
     */
    void setValue(Object newValue);

    /**
     * Return the value in the cell.
     *
     * @return the value in the cell.
     */
    Object getValue();
}
