package xianzhan.pascal.backend.interpreter.debugger;

import xianzhan.pascal.backend.interpreter.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Variable name and its value string pair used by the debugger.
 *
 * @author xianzhan
 * @since 2019-07-09
 */
public class NameValuePair {

    /**
     * variable's name
     */
    private String variableName;
    /**
     * variable's value string
     */
    private String valueString;

    /**
     * Constructor.
     *
     * @param variableName the variable's name
     * @param value        the variable's current value
     */
    protected NameValuePair(String variableName, Object value) {
        this.variableName = variableName;
        this.valueString = valueString(value);
    }

    private static final int MAX_DISPLAYED_ELEMENTS = 10;

    /**
     * Convert a value into a value string.
     *
     * @param value the value.
     * @return the value string.
     */
    protected static String valueString(Object value) {
        StringBuilder builder = new StringBuilder();

        // Undefined value.
        if (value == null) {
            builder.append('?');
        }

        // Dereference a VAR parameter.
        else if (value instanceof Cell) {
            builder.append(valueString(((Cell) value).getValue()));
        }

        // Array value.
        else if (value instanceof Cell[]) {
            arrayValueString((Cell[]) value, builder);
        }

        // Record value.
        else if (value instanceof HashMap) {
            recordValueString((HashMap) value, builder);
        }

        // Character value.
        else if (value instanceof Character) {
            builder.append('\'')
                    .append(value)
                    .append('\'');
        }

        // Numeric or boolean value.
        else {
            builder.append(value.toString());
        }

        return builder.toString();
    }

    /**
     * Convert an array value into a value string.
     *
     * @param array  the array.
     * @param buffer the StringBuilder to use.
     */
    private static void arrayValueString(Cell[] array, StringBuilder buffer) {
        int elementCount = 0;
        boolean first = true;
        buffer.append("[");

        // Loop over each array element up to MAX_DISPLAYED_ELEMENTS times.
        for (Cell cell : array) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }

            if (++elementCount <= MAX_DISPLAYED_ELEMENTS) {
                buffer.append(valueString(cell.getValue()));
            } else {
                buffer.append("...");
                break;
            }
        }

        buffer.append("]");
    }

    /**
     * Convert a record value into a value string.
     *
     * @param record the record.
     * @param buffer the StringBuilder to use.
     */
    private static void recordValueString(HashMap<String, Cell> record, StringBuilder buffer) {
        boolean first = true;
        buffer.append("{");

        Set<Map.Entry<String, Cell>> entries = record.entrySet();

        // Loop over each record field.
        for (Map.Entry<String, Cell> entry : entries) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }

            buffer.append(entry.getKey())
                    .append(": ")
                    .append(valueString(entry.getValue().getValue()));
        }

        buffer.append("}");
    }

    public String getVariableName() {
        return variableName;
    }

    public String getValueString() {
        return valueString;
    }
}
