package xianzhan.pascal.backend.interpreter.memory;

import xianzhan.pascal.backend.interpreter.Cell;

/**
 * The interpreter's runtime memory cell.
 *
 * @author xianzhan
 * @since 2019-07-03
 */
public class CellImpl implements Cell {

    /**
     * value contained in the memory cell
     */
    private Object value;

    /**
     * Constructor
     *
     * @param value the value for the cell.
     */
    public CellImpl(Object value) {
        this.value = value;
    }

    /**
     * Set a new value into the cell.
     *
     * @param newValue the new value.
     */
    @Override
    public void setValue(Object newValue) {
        this.value = newValue;
    }

    /**
     * Return the value in the cell.
     *
     * @return the value in the cell.
     */
    @Override
    public Object getValue() {
        return value;
    }
}
