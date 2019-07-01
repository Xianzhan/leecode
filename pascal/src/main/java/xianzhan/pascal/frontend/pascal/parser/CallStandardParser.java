package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.RoutineCode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.RoutineCodeEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;

/**
 * @author xianzhan
 * @since 2019-06-30
 */
public class CallStandardParser extends CallParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public CallStandardParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Parse a call to a declared procedure or function.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        ICodeNode callNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.CALL);
        SymTabEntry pfId = symTabStack.lookup(token.getText().toLowerCase());
        RoutineCode routineCode = (RoutineCode) pfId.getAttribute(SymTabKeyImpl.ROUTINE_CODE);
        callNode.setAttribute(ICodeKeyEnumImpl.ID, pfId);

        // consume procedure or function identifier
        token = nextToken();

        switch ((RoutineCodeEnumImpl) routineCode) {
            case READ:
            case READLN:
                return parseReadReadln(token, callNode, pfId);

            case WRITE:
            case WRITELN:
                return parseWriteWriteln(token, callNode, pfId);

            case EOF:
            case EOLN:
                return parseEofEoln(token, callNode, pfId);

            case ABS:
            case SQR:
                return parseAbsSqr(token, callNode, pfId);

            case ARCTAN:
            case COS:
            case EXP:
            case LN:
            case SIN:
            case SQRT:
                return parseArctanCosExpLnSinSqrt(token, callNode,
                        pfId);

            case PRED:
            case SUCC:
                return parsePredSucc(token, callNode, pfId);

            case CHR:
                return parseChr(token, callNode, pfId);
            case ODD:
                return parseOdd(token, callNode, pfId);
            case ORD:
                return parseOrd(token, callNode, pfId);

            case ROUND:
            case TRUNC:
                return parseRoundTrunc(token, callNode, pfId);

            default:
                // should never get here
                return null;
        }
    }

    /**
     * Parse a call to read or readln.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseReadReadln(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, true, false);
        callNode.addChild(paramsNode);

        // Read must have parameters.
        if ((pfId == Predefined.readId) && (callNode.getChildren().size() == 0)) {
            errorHandler.flag(token, PascalErrorCode.WRONG_NUMBER_OF_PARMS, this);
        }

        return callNode;
    }

    /**
     * Parse a call to write or writeln.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseWriteWriteln(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, false, true);
        callNode.addChild(paramsNode);

        // Write must have parameters.
        if ((pfId == Predefined.writeId) && (callNode.getChildren().size() == 0)) {
            errorHandler.flag(token, PascalErrorCode.WRONG_NUMBER_OF_PARMS, this);
        }

        return callNode;
    }

    /**
     * Parse a call to eof or eoln.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseEofEoln(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, false, false);
        callNode.addChild(paramsNode);

        // There should be no actual parameters.
        if (checkParamCount(token, paramsNode, 0)) {
            callNode.setTypeSpec(Predefined.booleanType);
        }

        return callNode;
    }

    /**
     * Parse a call to abs or sqr.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseAbsSqr(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, false, false);
        callNode.addChild(paramsNode);

        // There should be one integer or real parameter.
        // The function return type is the parameter type.
        if (checkParamCount(token, paramsNode, 1)) {
            TypeSpec argType = paramsNode.getChildren().get(0).getTypeSpec().baseType();

            if ((argType == Predefined.integerType) || (argType == Predefined.realType)) {
                callNode.setTypeSpec(argType);
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_TYPE, this);
            }
        }

        return callNode;
    }

    /**
     * Parse a call to arctan, cos, exp, ln, sin, or sqrt.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseArctanCosExpLnSinSqrt(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, false, false);
        callNode.addChild(paramsNode);

        // There should be one integer or real parameter.
        // The function return type is real.
        if (checkParamCount(token, paramsNode, 1)) {
            TypeSpec argType = paramsNode.getChildren().get(0).getTypeSpec().baseType();

            if ((argType == Predefined.integerType) || (argType == Predefined.realType)) {
                callNode.setTypeSpec(Predefined.realType);
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_TYPE, this);
            }
        }

        return callNode;
    }

    /**
     * Parse a call to pred or succ.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parsePredSucc(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, false, false);
        callNode.addChild(paramsNode);

        // There should be one integer or enumeration parameter.
        // The function return type is the parameter type.
        if (checkParamCount(token, paramsNode, 1)) {
            TypeSpec argType = paramsNode.getChildren().get(0).getTypeSpec().baseType();

            if ((argType == Predefined.integerType) || (argType.getForm() == TypeFormEnumImpl.ENUMERATION)) {
                callNode.setTypeSpec(argType);
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_TYPE, this);
            }
        }

        return callNode;
    }

    /**
     * Parse a call to chr.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseChr(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, false, false);
        callNode.addChild(paramsNode);

        // There should be one integer parameter.
        // The function return type is character.
        if (checkParamCount(token, paramsNode, 1)) {
            TypeSpec argType = paramsNode.getChildren().get(0).getTypeSpec().baseType();

            if (argType == Predefined.integerType) {
                callNode.setTypeSpec(Predefined.charType);
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_TYPE, this);
            }
        }

        return callNode;
    }

    /**
     * Parse a call to odd.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseOdd(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, false, false);
        callNode.addChild(paramsNode);

        // There should be one integer parameter.
        // The function return type is boolean.
        if (checkParamCount(token, paramsNode, 1)) {
            TypeSpec argType = paramsNode.getChildren().get(0).getTypeSpec().baseType();

            if (argType == Predefined.integerType) {
                callNode.setTypeSpec(Predefined.booleanType);
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_TYPE, this);
            }
        }

        return callNode;
    }

    /**
     * Parse a call to ord.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseOrd(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, false, false);
        callNode.addChild(paramsNode);

        // There should be one character or enumeration parameter.
        // The function return type is integer.
        if (checkParamCount(token, paramsNode, 1)) {
            TypeSpec argType = paramsNode.getChildren().get(0).getTypeSpec().baseType();

            if ((argType == Predefined.charType) || (argType.getForm() == TypeFormEnumImpl.ENUMERATION)) {
                callNode.setTypeSpec(Predefined.integerType);
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_TYPE, this);
            }
        }

        return callNode;
    }

    /**
     * Parse a call to round or trunc.
     *
     * @param token    the current token.
     * @param callNode the CALL node.
     * @param pfId     the symbol table entry of the standard routine name.
     * @return ICodeNode the CALL node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseRoundTrunc(Token token, ICodeNode callNode, SymTabEntry pfId) throws Exception {
        // Parse any actual parameters.
        ICodeNode paramsNode = parseActualParameters(token, pfId, false, false, false);
        callNode.addChild(paramsNode);

        // There should be one real parameter.
        // The function return type is integer.
        if (checkParamCount(token, paramsNode, 1)) {
            TypeSpec argType = paramsNode.getChildren().get(0).getTypeSpec().baseType();

            if (argType == Predefined.realType) {
                callNode.setTypeSpec(Predefined.integerType);
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_TYPE, this);
            }
        }

        return callNode;
    }


    /**
     * Check the number of actual parameters.
     *
     * @param token      the current token.
     * @param paramsNode the PARAMETERS node.
     * @param count      the correct number of parameters.
     * @return true if the count is correct.
     */
    private boolean checkParamCount(Token token, ICodeNode paramsNode, int count) {
        if (((paramsNode == null) && (count == 0)) || (paramsNode.getChildren().size() == count)) {
            return true;
        } else {
            errorHandler.flag(token, PascalErrorCode.WRONG_NUMBER_OF_PARMS, this);
            return false;
        }
    }
}
