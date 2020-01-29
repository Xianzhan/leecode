package xianzhan.pascal.backend.interpreter.executor;

import xianzhan.pascal.backend.interpreter.Cell;
import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.backend.interpreter.RuntimeErrorCode;
import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.RoutineCode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.RoutineCodeEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;

import java.util.ArrayList;

/**
 * Execute a call a standard procedure or function.
 *
 * @author xianzhan
 * @since 2019-07-03
 */
public class CallStandardExecutor extends CallExecutor {

    private ExpressionExecutor expressionExecutor;

    /**
     * Constructor
     *
     * @param parent the parent executor.
     */
    public CallStandardExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute a call to a standard procedure or function.
     *
     * @param node the CALL node.
     * @return the function value, or null for a procedure call.
     */
    @Override
    public Object execute(ICodeNode node) {
        SymTabEntry routineId = (SymTabEntry) node.getAttribute(ICodeKeyEnumImpl.ID);
        RoutineCode routineCode = (RoutineCode) routineId.getAttribute(SymTabKeyImpl.ROUTINE_CODE);
        TypeSpec type = node.getTypeSpec();
        expressionExecutor = new ExpressionExecutor(this);
        ICodeNode actualNode = null;

        // Get the actual parameters of the call.
        if (node.getChildren().size() > 0) {
            ICodeNode paramsNode = node.getChildren().get(0);
            actualNode = paramsNode.getChildren().get(0);
        }

        switch ((RoutineCodeEnumImpl) routineCode) {
            case READ:
            case READLN:
                return executeReadReadln(node, routineCode);

            case WRITE:
            case WRITELN:
                return executeWriteWriteln(node, routineCode);

            case EOF:
            case EOLN:
                return executeEofEoln(node, routineCode);

            case ABS:
            case SQR:
                return executeAbsSqr(node, routineCode, actualNode);

            case ARCTAN:
            case COS:
            case EXP:
            case LN:
            case SIN:
            case SQRT:
                return executeArctanCosExpLnSinSqrt(node, routineCode, actualNode);

            case PRED:
            case SUCC:
                return executePredSucc(node, routineCode, actualNode, type);

            case CHR:
                return executeChr(node, routineCode, actualNode);
            case ODD:
                return executeOdd(node, routineCode, actualNode);
            case ORD:
                return executeOrd(node, routineCode, actualNode);

            case ROUND:
            case TRUNC:
                return executeRoundTrunc(node, routineCode, actualNode);

            default:
                // should never get here
                return null;
        }
    }

    /**
     * Execute a call to read or readln.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @return null.
     */
    private Object executeReadReadln(ICodeNode callNode, RoutineCode routineCode) {
        ICodeNode paramsNode = callNode.getChildren().size() > 0
                ? callNode.getChildren().get(0)
                : null;

        if (paramsNode != null) {
            ArrayList<ICodeNode> actualList = paramsNode.getChildren();

            // Loop to process each actual parameter.
            for (ICodeNode actualNode : actualList) {
                TypeSpec type = actualNode.getTypeSpec();
                TypeSpec baseType = type.baseType();
                Cell variableCell = (Cell) expressionExecutor.executeVariable(actualNode);
                Object value;

                // Read a value of the appropriate type from the standard input.
                try {
                    if (baseType == Predefined.integerType) {
                        Token token = standardIn.nextToken();
                        value = parseNumber(token, baseType);
                    } else if (baseType == Predefined.realType) {
                        Token token = standardIn.nextToken();
                        value = parseNumber(token, baseType);
                    } else if (baseType == Predefined.booleanType) {
                        Token token = standardIn.nextToken();
                        value = parseBoolean(token);
                    } else if (baseType == Predefined.charType) {
                        char ch = standardIn.nextChar();
                        if ((ch == Source.EOL) || (ch == Source.EOF)) {
                            ch = ' ';
                        }
                        value = ch;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception ex) {
                    errorHandler.flag(callNode, RuntimeErrorCode.INVALID_INPUT, CallStandardExecutor.this);

                    if (type == Predefined.realType) {
                        value = 0.0f;
                    } else if (type == Predefined.charType) {
                        value = ' ';
                    } else if (type == Predefined.booleanType) {
                        value = false;
                    } else {
                        value = 0;
                    }
                }

                // Range check and set the value.
                value = checkRange(callNode, type, value);
                variableCell.setValue(value);

                SymTabEntry actualId = (SymTabEntry) actualNode.getAttribute(ICodeKeyEnumImpl.ID);
                sendAssignMessage(callNode, actualId.getName(), value);
            }
        }

        // Skip the rest of the input line for readln.
        if (routineCode == RoutineCodeEnumImpl.READLN) {
            try {
                standardIn.skipToNextLine();
            } catch (Exception ex) {
                errorHandler.flag(callNode, RuntimeErrorCode.INVALID_INPUT, CallStandardExecutor.this);
            }
        }

        return null;
    }

    /**
     * Parse an integer or real value from the standard input.
     *
     * @param token the current input token.
     * @param type  the input value type.
     * @return the integer or real value.
     * @throws Exception if an error occurred.
     */
    private Number parseNumber(Token token, TypeSpec type) throws Exception {
        TokenType tokenType = token.getType();
        TokenType sign = null;

        // Leading sign?
        if ((tokenType == PascalTokenType.PLUS) || (tokenType == PascalTokenType.MINUS)) {
            sign = tokenType;
            token = standardIn.nextToken();
            tokenType = token.getType();
        }

        // Integer value.
        if (tokenType == PascalTokenType.INTEGER) {
            Number value = sign == PascalTokenType.MINUS
                    ? -((Integer) token.getValue())
                    : (Integer) token.getValue();
            return type == Predefined.integerType
                    ? value
                    : value.floatValue();
        }

        // Real value.
        else if (tokenType == PascalTokenType.REAL) {
            Number value = sign == PascalTokenType.MINUS
                    ? -((Float) token.getValue())
                    : (Float) token.getValue();
            return type == Predefined.realType
                    ? value
                    : value.intValue();
        }

        // Bad input.
        else {
            throw new Exception();
        }
    }

    /**
     * Parse a boolean value from the standard input.
     *
     * @param token the current input token.
     * @return the boolean value.
     * @throws Exception if an error occurred.
     */
    private Boolean parseBoolean(Token token) throws Exception {
        if (token.getType() == PascalTokenType.IDENTIFIER) {
            String text = token.getText();

            if ("true".equalsIgnoreCase(text)) {
                return Boolean.TRUE;
            } else if ("false".equalsIgnoreCase(text)) {
                return Boolean.FALSE;
            } else {
                throw new Exception();
            }
        } else {
            throw new Exception();
        }
    }

    /**
     * Execute a call to write or writeln.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @return null.
     */
    private Object executeWriteWriteln(ICodeNode callNode, RoutineCode routineCode) {
        ICodeNode paramsNode = callNode.getChildren().size() > 0
                ? callNode.getChildren().get(0)
                : null;

        if (paramsNode != null) {
            ArrayList<ICodeNode> actualList = paramsNode.getChildren();

            // Loop to process each WRITE_PARM actual parameter node.
            for (ICodeNode writeParamNode : actualList) {
                ArrayList<ICodeNode> children = writeParamNode.getChildren();
                ICodeNode exprNode = children.get(0);
                TypeSpec dataType = exprNode.getTypeSpec().baseType();
                String typeCode = dataType.isPascalString()
                        ? "s" : dataType == Predefined.integerType
                        ? "d" : dataType == Predefined.realType
                        ? "f" : dataType == Predefined.booleanType
                        ? "s" : dataType == Predefined.charType
                        ? "c" : "s";
                Object value = expressionExecutor.execute(exprNode);

                if ((dataType == Predefined.charType) && (value instanceof String)) {
                    value = ((String) value).charAt(0);
                }

                // Java format string.
                StringBuilder format = new StringBuilder("%");

                // Process any field width and precision values.
                if (children.size() > 1) {
                    int w = (Integer) children.get(1).getAttribute(ICodeKeyEnumImpl.VALUE);
                    format.append(w == 0 ? 1 : w);
                }
                if (children.size() > 2) {
                    int p = (Integer) children.get(2).getAttribute(ICodeKeyEnumImpl.VALUE);
                    format.append(".");
                    format.append(p == 0 ? 1 : p);
                }

                format.append(typeCode);

                // Write the formatted value to the standard output.
                standardOut.printf(format.toString(), value);
                standardOut.flush();
            }
        }

        // Line feed for writeln.
        if (routineCode == RoutineCodeEnumImpl.WRITELN) {
            standardOut.println();
            standardOut.flush();
        }

        return null;
    }

    /**
     * Execute a call to eof or eoln.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @return true or false.
     */
    private Boolean executeEofEoln(ICodeNode callNode, RoutineCode routineCode) {
        try {
            if (routineCode == RoutineCodeEnumImpl.EOF) {
                return standardIn.atEof();
            } else {
                return standardIn.atEol();
            }
        } catch (Exception ex) {
            errorHandler.flag(callNode, RuntimeErrorCode.INVALID_INPUT, CallStandardExecutor.this);
            return true;
        }
    }

    /**
     * Execute a call to abs or sqr.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @param actualNode  the actual parameter node.
     * @return the function value.
     */
    private Number executeAbsSqr(ICodeNode callNode, RoutineCode routineCode, ICodeNode actualNode) {
        Object argValue = expressionExecutor.execute(actualNode);

        if (argValue instanceof Integer) {
            int value = (Integer) argValue;
            return routineCode == RoutineCodeEnumImpl.ABS ? Math.abs(value) : value * value;
        } else {
            float value = (Float) argValue;
            return routineCode == RoutineCodeEnumImpl.ABS ? Math.abs(value) : value * value;
        }
    }

    /**
     * Execute a call to arctan, cos, exp, ln, sin, or sqrt.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @param actualNode  the actual parameter node.
     * @return the function value.
     */
    private Float executeArctanCosExpLnSinSqrt(ICodeNode callNode, RoutineCode routineCode, ICodeNode actualNode) {
        Object argValue = expressionExecutor.execute(actualNode);
        float value = argValue instanceof Integer
                ? (Integer) argValue
                : (Float) argValue;

        switch ((RoutineCodeEnumImpl) routineCode) {
            case ARCTAN:
                return (float) Math.atan(value);
            case COS:
                return (float) Math.cos(value);
            case EXP:
                return (float) Math.exp(value);
            case SIN:
                return (float) Math.sin(value);

            case LN: {
                if (value > 0.0F) {
                    return (float) Math.log(value);
                } else {
                    errorHandler.flag(callNode, RuntimeErrorCode.INVALID_STANDARD_FUNCTION_ARGUMENT, CallStandardExecutor.this);
                    return 0.0F;
                }
            }

            case SQRT: {
                if (value >= 0.0F) {
                    return (float) Math.sqrt(value);
                } else {
                    errorHandler.flag(callNode, RuntimeErrorCode.INVALID_STANDARD_FUNCTION_ARGUMENT, CallStandardExecutor.this);
                    return 0.0F;
                }
            }

            default:
                // should never get here
                return 0.0F;
        }
    }

    /**
     * Execute a call to pred or succ.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @param actualNode  the actual parameter node.
     * @param type        the value type.
     * @return the function value.
     */
    private Integer executePredSucc(ICodeNode callNode, RoutineCode routineCode, ICodeNode actualNode, TypeSpec type) {
        int value = (Integer) expressionExecutor.execute(actualNode);
        int newValue = routineCode == RoutineCodeEnumImpl.PRED ? --value : ++value;

        newValue = (Integer) checkRange(callNode, type, newValue);
        return newValue;
    }

    /**
     * Execute a call to chr.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @param actualNode  the actual parameter node.
     * @return the function value.
     */
    private Character executeChr(ICodeNode callNode, RoutineCode routineCode, ICodeNode actualNode) {
        int value = (Integer) expressionExecutor.execute(actualNode);
        return (char) value;
    }

    /**
     * Execute a call to odd.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @param actualNode  the actual parameter node.
     * @return true or false.
     */
    private Boolean executeOdd(ICodeNode callNode, RoutineCode routineCode, ICodeNode actualNode) {
        int value = (Integer) expressionExecutor.execute(actualNode);
        return (value & 1) == 1;
    }

    /**
     * Execute a call to ord.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @param actualNode  the actual parameter node.
     * @return the function value.
     */
    private Integer executeOrd(ICodeNode callNode, RoutineCode routineCode, ICodeNode actualNode) {
        Object value = expressionExecutor.execute(actualNode);

        if (value instanceof Character) {
            char ch = (Character) value;
            return (int) ch;
        } else if (value instanceof String) {
            char ch = ((String) value).charAt(0);
            return (int) ch;
        } else {
            return (Integer) value;
        }
    }

    /**
     * Execute a call to round or trunc.
     *
     * @param callNode    the CALL node.
     * @param routineCode the routine code.
     * @param actualNode  the actual parameter node.
     * @return the function value.
     */
    private Integer executeRoundTrunc(ICodeNode callNode, RoutineCode routineCode, ICodeNode actualNode) {
        float value = (Float) expressionExecutor.execute(actualNode);

        if (routineCode == RoutineCodeEnumImpl.ROUND) {
            return value >= 0.0f
                    ? (int) (value + 0.5f)
                    : (int) (value - 0.5f);
        } else {
            return (int) value;
        }
    }
}
