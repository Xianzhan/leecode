package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.RoutineCode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeForm;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.RoutineCodeEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.intermediate.impl.TypeChecker;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Parse a called to a procedure or function.
 *
 * @author xianzhan
 * @since 2019-06-30
 */
public class CallParser extends StatementParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public CallParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Parse a call to a declared procedure or function.
     *
     * <pre>
     * PROCEDURE proc (j, k : integer; VAR x, y, z : real; VAR v : arr;
     *     VAR p : boolean; ch : char);
     *     BEGIN
     *         ...
     *     END;
     *
     * PROCEDURE SortWords;
     *     BEGIN
     *         ...
     *     END;
     *
     * FUNCTION func (VAR x : real; i, n : integer) : real;
     *     BEGIN
     *         ...
     *         func := ...;
     *         ...
     *     END;
     * </pre>
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        SymTabEntry pfId = symTabStack.lookup(token.getText().toLowerCase());
        RoutineCode routineCode = (RoutineCode) pfId.getAttribute(SymTabKeyImpl.ROUTINE_CODE);
        StatementParser callParser = (routineCode == RoutineCodeEnumImpl.DECLARED || routineCode == RoutineCodeEnumImpl.FORWARD)
                ? new CallDeclaredParser(this)
                : new CallStandardParser(this);

        return callParser.parse(token);
    }

    /**
     * Synchronization set for the , token.
     */
    private static final EnumSet<PascalTokenType> COMMA_SET =
            ExpressionParser.EXPR_START_SET.clone();

    static {
        COMMA_SET.add(PascalTokenType.COMMA);
        COMMA_SET.add(PascalTokenType.RIGHT_PAREN);
    }

    /**
     * Parse the actual parameters of a procedure or function call.
     *
     * @param token          the current token.
     * @param pfId           the symbol table entry of the procedure or function name.
     * @param isDeclared     true if parsing actual params of a declared routine.
     * @param isReadReadln   true if parsing actual params of read or readln.
     * @param isWriteWriteln true if parsing actual params of write or writeln.
     * @return the PARAMETERS node, or null if there are no actual parameters.
     * @throws Exception if an error occurred.
     */
    protected ICodeNode parseActualParameters(Token token,
                                              SymTabEntry pfId,
                                              boolean isDeclared,
                                              boolean isReadReadln,
                                              boolean isWriteWriteln) throws Exception {

        ExpressionParser expressionParser = new ExpressionParser(this);
        ICodeNode parametersNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.PARAMETERS);
        ArrayList<SymTabEntry> formalParams = null;
        int parameterCount = 0;
        int parameterIndex = -1;

        if (isDeclared) {
            formalParams = (ArrayList<SymTabEntry>) pfId.getAttribute(SymTabKeyImpl.ROUTINE_PARMS);
            parameterCount = formalParams != null ? formalParams.size() : 0;
        }

        if (token.getType() != PascalTokenType.LEFT_PAREN) {
            if (parameterCount != 0) {
                errorHandler.flag(token, PascalErrorCode.WRONG_NUMBER_OF_PARMS, this);
            }

            return null;
        }

        // consume opening (
        token = nextToken();

        // Loop to parse each actual parameter.
        while (token.getType() != PascalTokenType.RIGHT_PAREN) {
            ICodeNode actualNode = expressionParser.parse(token);

            // Declared procedure or function: Check the number of actual
            // parameters, and check each actual parameter against the
            // corresponding formal parameter.
            if (isDeclared) {
                if (++parameterIndex < parameterCount) {
                    SymTabEntry formalId = formalParams.get(parameterIndex);
                    checkActualParameter(token, formalId, actualNode);
                } else if (parameterIndex == parameterCount) {
                    errorHandler.flag(token, PascalErrorCode.WRONG_NUMBER_OF_PARMS, this);
                }
            }

            // read or readln: Each actual parameter must be a variable that is
            //                 a scalar, boolean, or subrange of integer.
            else if (isReadReadln) {
                TypeSpec type = actualNode.getTypeSpec();
                TypeForm form = type.getForm();

                if (!((actualNode.getType() == ICodeNodeTypeEnumImpl.VARIABLE)
                        && ((form == TypeFormEnumImpl.SCALAR) || (type == Predefined.booleanType)
                        || ((form == TypeFormEnumImpl.SUBRANGE) && (type.baseType() == Predefined.integerType))))) {
                    errorHandler.flag(token, PascalErrorCode.INVALID_VAR_PARM, this);
                }
            }

            // write or writeln: The type of each actual parameter must be a
            // scalar, boolean, or a Pascal string. Parse any field width and
            // precision.
            else if (isWriteWriteln) {

                // Create a WRITE_PARM node which adopts the expression node.
                ICodeNode exprNode = actualNode;
                actualNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.WRITE_PARM);
                actualNode.addChild(exprNode);

                TypeSpec type = exprNode.getTypeSpec().baseType();
                TypeForm form = type.getForm();

                if (!((form == TypeFormEnumImpl.SCALAR) || (type == Predefined.booleanType) || (type.isPascalString()))) {
                    errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                }

                // Optional field width.
                token = currentToken();
                actualNode.addChild(parseWriteSpec(token));

                // Optional precision.
                token = currentToken();
                actualNode.addChild(parseWriteSpec(token));
            }

            parametersNode.addChild(actualNode);
            token = synchronize(COMMA_SET);
            TokenType tokenType = token.getType();

            // Look for the comma.
            if (tokenType == PascalTokenType.COMMA) {
                // consume ,
                token = nextToken();
            } else if (ExpressionParser.EXPR_START_SET.contains(tokenType)) {
                errorHandler.flag(token, PascalErrorCode.MISSING_COMMA, this);
            } else if (tokenType != PascalTokenType.RIGHT_PAREN) {
                token = synchronize(ExpressionParser.EXPR_START_SET);
            }
        }

        // consume closing )
        token = nextToken();

        if ((parametersNode.getChildren().size() == 0) || (isDeclared && (parameterIndex != parameterCount - 1))) {
            errorHandler.flag(token, PascalErrorCode.WRONG_NUMBER_OF_PARMS, this);
        }

        return parametersNode;
    }

    /**
     * Check an actual parameter against the corresponding formal parameter.
     *
     * @param token      the current token.
     * @param formalId   the symbol table entry of the formal parameter.
     * @param actualNode the parse tree node of the actual parameter.
     */
    private void checkActualParameter(Token token, SymTabEntry formalId, ICodeNode actualNode) {
        Definition formalDefinition = formalId.getDefinition();
        TypeSpec formalType = formalId.getTypeSpec();
        TypeSpec actualType = actualNode.getTypeSpec();

        // VAR parameter: The actual parameter must be a variable of the same
        //                type as the formal parameter.
        if (formalDefinition == DefinitionEnumImpl.VAR_PARM) {
            if ((actualNode.getType() != ICodeNodeTypeEnumImpl.VARIABLE) || (actualType != formalType)) {
                errorHandler.flag(token, PascalErrorCode.INVALID_VAR_PARM, this);
            }
        }

        // Value parameter: The actual parameter must be assignment-compatible
        //                  with the formal parameter.
        else if (!TypeChecker.areAssignmentCompatible(formalType, actualType)) {
            errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
        }
    }

    /**
     * Parse the field width or the precision for an actual parameter
     * of a call to write or writeln.
     *
     * @param token the current token.
     * @return the INTEGER_CONSTANT node or null
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseWriteSpec(Token token) throws Exception {
        if (token.getType() == PascalTokenType.COLON) {
            // consume :
            token = nextToken();

            ExpressionParser expressionParser = new ExpressionParser(this);
            ICodeNode specNode = expressionParser.parse(token);

            if (specNode.getType() == ICodeNodeTypeEnumImpl.INTEGER_CONSTANT) {
                return specNode;
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_NUMBER, this);
                return null;
            }
        } else {
            return null;
        }
    }
}
