package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Cell;
import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.backend.interpreter.MemoryFactory;
import xianzhan.pascal.backend.interpreter.RuntimeErrorCode;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Execute a statement.
 *
 * @author xianzhan
 * @since 2019-06-05
 */
public class StatementExecutor extends Executor {

    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public StatementExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute a statement.
     * To be overridden by the specialized statement executor subclasses.
     *
     * @param node the root node of the statement.
     * @return null.
     */
    public Object execute(ICodeNode node) {
        ICodeNodeTypeEnumImpl nodeType = (ICodeNodeTypeEnumImpl) node.getType();

        // Send a message about the current source line.
        sendSourceLineMessage(node);

        switch (nodeType) {
            case COMPOUND: {
                CompoundExecutor compoundExecutor = new CompoundExecutor(this);
                return compoundExecutor.execute(node);
            }

            case ASSIGN: {
                AssignmentExecutor assignmentExecutor = new AssignmentExecutor(this);
                return assignmentExecutor.execute(node);
            }

            case LOOP: {
                LoopExecutor loopExecutor = new LoopExecutor(this);
                return loopExecutor.execute(node);
            }

            case IF: {
                IfExecutor ifExecutor = new IfExecutor(this);
                return ifExecutor.execute(node);
            }

            case SELECT: {
                SelectExecutor selectExecutor = new SelectExecutor(this);
                return selectExecutor.execute(node);
            }

            case CALL: {
                CallExecutor callExecutor = new CallExecutor(this);
                return callExecutor.execute(node);
            }

            case NO_OP:
                return null;

            default: {
                errorHandler.flag(node, RuntimeErrorCode.UNIMPLEMENTED_FEATURE, this);
                return null;
            }
        }
    }

    /**
     * Convert a Java string to a Pascal string or character.
     *
     * @param targetType the target type specification.
     * @param javaValue  the Java string.
     * @return the Pascal string or character.
     */
    protected Object toPascal(TypeSpec targetType, Object javaValue) {
        if (javaValue instanceof String) {
            String string = (String) javaValue;

            if (targetType == Predefined.charType) {
                // Pascal character
                return string.charAt(0);
            } else if (targetType.isPascalString()) {
                Cell[] charCells = new Cell[string.length()];

                // Build an array of characters.
                for (int i = 0; i < string.length(); ++i) {
                    charCells[i] = MemoryFactory.createCell(string.charAt(i));
                }

                // Pascal string (array of characters)
                return charCells;
            } else {
                return javaValue;
            }
        } else {
            return javaValue;
        }
    }

    /**
     * Convert a Pascal string to a Java string.
     *
     * @param targetType  the target type specification
     * @param pascalValue the Pascal string.
     * @return the Java string.
     */
    protected Object toJava(TypeSpec targetType, Object pascalValue) {
        if ((pascalValue instanceof Cell[]) &&
                (((Cell[]) pascalValue)[0].getValue() instanceof Character)) {
            Cell[] charCells = (Cell[]) pascalValue;
            StringBuilder string = new StringBuilder(charCells.length);

            // Build a Java string.
            for (Cell ref : charCells) {
                string.append(ref.getValue());
            }

            // Java string
            return string.toString();
        } else {
            return pascalValue;
        }
    }

    /**
     * Return a copy of a Pascal value.
     *
     * @param value the value.
     * @param node  the statement node.
     * @return the copy.
     */
    protected Object copyOf(Object value, ICodeNode node) {
        Object copy;

        if (value instanceof Integer) {
            copy = value;
        } else if (value instanceof Float) {
            copy = value;
        } else if (value instanceof Character) {
            copy = value;
        } else if (value instanceof Boolean) {
            copy = value;
        } else if (value instanceof String) {
            copy = value;
        } else if (value instanceof HashMap) {
            copy = copyRecord((HashMap<String, Object>) value, node);
        } else {
            copy = copyArray((Cell[]) value, node);
        }

        return copy;
    }

    /**
     * Return a copy of a Pascal record.
     *
     * @param value the record value hash map.
     * @param node  the statement node.
     * @return the copy of the hash map.
     */
    private Object copyRecord(HashMap<String, Object> value, ICodeNode node) {
        HashMap<String, Object> copy = new HashMap<>();

        if (value != null) {
            Set<Map.Entry<String, Object>> entries = value.entrySet();

            for (Map.Entry<String, Object> entry : entries) {
                String newKey = entry.getKey();
                Cell valueCell = (Cell) entry.getValue();
                Object newValue = copyOf(valueCell.getValue(), node);

                copy.put(newKey, MemoryFactory.createCell(newValue));
            }
        } else {
            errorHandler.flag(node, RuntimeErrorCode.UNINITIALIZED_VALUE, this);
        }

        return copy;
    }

    /**
     * Return a copy of a Pascal array.
     *
     * @param valueCells the array cells.
     * @param node       the statement node.
     * @return the copy of the array cells.
     */
    private Cell[] copyArray(Cell[] valueCells, ICodeNode node) {
        int length;
        Cell[] copy;

        if (valueCells != null) {
            length = valueCells.length;
            copy = new Cell[length];

            for (int i = 0; i < length; ++i) {
                Cell valueCell = valueCells[i];
                Object newValue = copyOf(valueCell.getValue(), node);
                copy[i] = MemoryFactory.createCell(newValue);
            }
        } else {
            errorHandler.flag(node, RuntimeErrorCode.UNINITIALIZED_VALUE, this);
            copy = new Cell[1];
        }

        return copy;
    }

    /**
     * Runtime range check.
     *
     * @param node  the root node of the expression subtree to check.
     * @param type  the target type specification.
     * @param value the value.
     * @return the value to use.
     */
    protected Object checkRange(ICodeNode node, TypeSpec type, Object value) {
        if (type.getForm() == TypeFormEnumImpl.SUBRANGE) {
            int minValue = (Integer) type.getAttribute(TypeKeyEnumImpl.SUBRANGE_MIN_VALUE);
            int maxValue = (Integer) type.getAttribute(TypeKeyEnumImpl.SUBRANGE_MAX_VALUE);

            if (((Integer) value) < minValue) {
                errorHandler.flag(node, RuntimeErrorCode.VALUE_RANGE, this);
                return minValue;
            } else if (((Integer) value) > maxValue) {
                errorHandler.flag(node, RuntimeErrorCode.VALUE_RANGE, this);
                return maxValue;
            } else {
                return value;
            }
        } else {
            return value;
        }
    }

    /**
     * Send a message about the current source line.
     *
     * @param node the statement node.
     */
    private void sendSourceLineMessage(ICodeNode node) {
        Object lineNumber = node.getAttribute(ICodeKeyEnumImpl.LINE);

        // Send the SOURCE_LINE message.
        if (lineNumber != null) {
            sendMessage(new Message(MessageType.SOURCE_LINE, lineNumber));
        }
    }

    /**
     * Send a message about an assignment operation.
     *
     * @param node         the parse tree node.
     * @param variableName the name of the target variable.
     * @param value        the value of the expression.
     */
    protected void sendAssignMessage(ICodeNode node, String variableName, Object value) {
        Object lineNumber = getLineNumber(node);

        // Send an ASSIGN message.
        if (lineNumber != null) {
            sendMessage(new Message(MessageType.ASSIGN, new Object[]{lineNumber, variableName, value}));
        }
    }

    /**
     * Send a message about a value fetch operation.
     *
     * @param node         the parse tree node.
     * @param variableName the name of the variable.
     * @param value        the value of the expression.
     */
    protected void sendFetchMessage(ICodeNode node, String variableName, Object value) {
        Object lineNumber = getLineNumber(node);

        // Send a FETCH message.
        if (lineNumber != null) {
            sendMessage(new Message(MessageType.FETCH, new Object[]{lineNumber, variableName, value}));
        }
    }

    /**
     * Send a message about a call to a declared procedure or function.
     *
     * @param node the parse tree node.
     */
    protected void sendCallMessage(ICodeNode node, String routineName) {
        Object lineNumber = getLineNumber(node);

        // Send a CALL message.
        if (lineNumber != null) {
            sendMessage(new Message(MessageType.CALL, new Object[]{lineNumber, routineName}));
        }
    }

    /**
     * Send a message about a return from a declared procedure or function.
     *
     * @param node the parse tree node.
     */
    protected void sendReturnMessage(ICodeNode node, String routineName) {
        Object lineNumber = getLineNumber(node);

        // Send a RETURN message.
        if (lineNumber != null) {
            sendMessage(new Message(MessageType.RETURN, new Object[]{lineNumber, routineName}));
        }
    }

    /**
     * Get the source line number of a parse tree node.
     *
     * @param node the parse tree node.
     * @return the line number.
     */
    private Object getLineNumber(ICodeNode node) {
        Object lineNumber = null;

        // Go up the parent links to look for a line number.
        while ((node != null) && ((lineNumber = node.getAttribute(ICodeKeyEnumImpl.LINE)) == null)) {
            node = node.getParent();
        }

        return lineNumber;
    }
}
