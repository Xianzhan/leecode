package xianzhan.pascal.backend.interpreter.debugger;

import xianzhan.pascal.backend.interpreter.Cell;
import xianzhan.pascal.backend.interpreter.Debugger;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeForm;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Memory cell and data type pair used by the debugger.
 *
 * @author xianzhan
 * @since 2019-07-09
 */
public class CellTypePair {

    /**
     * Memory cell
     */
    private Cell     cell;
    /**
     * Data type
     */
    private TypeSpec type;
    /**
     * Parent debugger
     */
    private Debugger debugger;

    public CellTypePair(Cell cell, TypeSpec type, Debugger debugger) throws Exception {
        this.cell = cell;
        this.type = type;
        this.debugger = debugger;

        parseVariable();
    }

    protected Cell getCell() {
        return cell;
    }

    protected TypeSpec getType() {
        return type;
    }

    /**
     * Synchronization set for variable modifiers.
     */
    protected static final EnumSet<PascalTokenType> MODIFIER_SET =
            EnumSet.of(
                    PascalTokenType.LEFT_BRACKET,
                    PascalTokenType.DOT
            );

    /**
     * Parse a variable in the command to obtain it's memory cell.
     *
     * @throws Exception if an error occurred.
     */
    protected void parseVariable() throws Exception {
        TypeForm form = type.getForm();
        Object value = cell.getValue();

        // Loop to process array subscripts and record fields.
        while (MODIFIER_SET.contains(debugger.currentToken().getType())) {
            if (form == TypeFormEnumImpl.ARRAY) {
                parseArrayVariable((Cell[]) value);
            } else if (form == TypeFormEnumImpl.RECORD) {
                parseRecordVariable((HashMap) value);
            }

            value = cell.getValue();
            form = type.getForm();
        }
    }

    /**
     * Parse an array variable.
     *
     * @param array the array variable
     * @throws Exception if an error occurred.
     */
    private void parseArrayVariable(Cell[] array) throws Exception {
        debugger.nextToken();

        int index = debugger.getInteger("Integer index expected.");
        int minValue = 0;
        TypeSpec indexType = (TypeSpec) type.getAttribute(TypeKeyEnumImpl.ARRAY_INDEX_TYPE);

        rangeCheck(index, indexType, "Index out of range.");
        type = (TypeSpec) type.getAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_TYPE);

        if (indexType.getForm() == TypeFormEnumImpl.SUBRANGE) {
            minValue = (int) indexType.getAttribute(TypeKeyEnumImpl.SUBRANGE_MIN_VALUE);
        }

        cell = array[index - minValue];

        if (debugger.currentToken().getType() == PascalTokenType.RIGHT_BRACKET) {
            debugger.nextToken();
        } else {
            throw new Exception("] expected.");
        }
    }

    /**
     * Parse a record variable.
     *
     * @param record the record variable
     * @throws Exception if an error occurred.
     */
    private void parseRecordVariable(HashMap record) throws Exception {
        debugger.nextToken();

        String fieldName = debugger.getWord("Field name expected.");

        if (record.containsKey(fieldName)) {
            cell = (Cell) record.get(fieldName);
        } else {
            throw new Exception("Invalid field name.");
        }

        SymTab symTab = (SymTab) type.getAttribute(TypeKeyEnumImpl.RECORD_SYMTAB);
        SymTabEntry id = symTab.lookup(fieldName);
        type = id.getTypeSpec();
    }

    /**
     * Set the value of the cell.
     *
     * @param value the value
     * @throws Exception if an error occurred.
     */
    protected void setValue(Object value) throws Exception {
        if ((type.baseType() == Predefined.integerType && value instanceof Integer)
                || (type == Predefined.realType && value instanceof Float)
                || (type == Predefined.booleanType && value instanceof Boolean)
                || (type == Predefined.charType && value instanceof Character)
        ) {
            if (type.baseType() == Predefined.integerType) {
                rangeCheck((Integer) value, type, "Value out of range.");
            }
            cell.setValue(value);
        } else {
            throw new Exception("Type mismatch");
        }
    }

    /**
     * Do a range check on an integer value.
     *
     * @param value        the value.
     * @param type         the data type.
     * @param errorMessage the error message if an exception is thrown.
     * @throws Exception if an error occurred.
     */
    private void rangeCheck(int value, TypeSpec type, String errorMessage) throws Exception {
        TypeForm form = type.getForm();
        Integer minValue = null;
        Integer maxValue = null;

        if (form == TypeFormEnumImpl.SUBRANGE) {
            minValue = (Integer) type.getAttribute(TypeKeyEnumImpl.SUBRANGE_MIN_VALUE);
            maxValue = (Integer) type.getAttribute(TypeKeyEnumImpl.SUBRANGE_MAX_VALUE);
        } else if (form == TypeFormEnumImpl.ENUMERATION) {
            ArrayList<SymTabEntry> constants = (ArrayList<SymTabEntry>) type.getAttribute(TypeKeyEnumImpl.ENUMERATION_CONSTANTS);
            minValue = 0;
            maxValue = constants.size() - 1;
        }

        if ((minValue != null) && ((value < minValue) || (value > maxValue))) {
            throw new Exception(errorMessage);
        }
    }
}
