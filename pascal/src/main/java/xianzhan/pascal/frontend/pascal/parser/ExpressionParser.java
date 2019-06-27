package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.ICodeNodeType;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeFactory;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.intermediate.impl.TypeChecker;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * Parse a Pascal expression.
 *
 * @author xianzhan
 * @since 2019-06-03
 */
public class ExpressionParser extends StatementParser {

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public ExpressionParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set for starting an expression.
     */
    static final EnumSet<PascalTokenType> EXPR_START_SET =
            EnumSet.of(
                    PascalTokenType.PLUS,
                    PascalTokenType.MINUS,
                    PascalTokenType.IDENTIFIER,
                    PascalTokenType.INTEGER,
                    PascalTokenType.REAL,
                    PascalTokenType.STRING,
                    PascalTokenType.NOT,
                    PascalTokenType.LEFT_PAREN
            );

    /**
     * Parse an expression.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        return parseExpression(token);
    }

    /**
     * Set of relational operators.
     */
    private static final EnumSet<PascalTokenType> REL_OPS =
            EnumSet.of(PascalTokenType.EQUALS,
                    PascalTokenType.NOT_EQUALS,
                    PascalTokenType.LESS_THAN,
                    PascalTokenType.LESS_EQUALS,
                    PascalTokenType.GREATER_THAN,
                    PascalTokenType.GREATER_EQUALS);

    /**
     * Map relational operator tokens to node types.
     */
    private static final HashMap<PascalTokenType, ICodeNodeType> REL_OPS_MAP = new HashMap<>();

    static {
        REL_OPS_MAP.put(PascalTokenType.EQUALS, ICodeNodeTypeEnumImpl.EQ);
        REL_OPS_MAP.put(PascalTokenType.NOT_EQUALS, ICodeNodeTypeEnumImpl.NE);
        REL_OPS_MAP.put(PascalTokenType.LESS_THAN, ICodeNodeTypeEnumImpl.LT);
        REL_OPS_MAP.put(PascalTokenType.LESS_EQUALS, ICodeNodeTypeEnumImpl.LE);
        REL_OPS_MAP.put(PascalTokenType.GREATER_THAN, ICodeNodeTypeEnumImpl.GT);
        REL_OPS_MAP.put(PascalTokenType.GREATER_EQUALS, ICodeNodeTypeEnumImpl.GE);
    }

    /**
     * Parse an expression.
     *
     * @param token the initial token.
     * @return the root of the generated parse subtree.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseExpression(Token token) throws Exception {
        // Parse a simple expression and make the root of it's tree
        // the root node.
        ICodeNode rootNode = parseSimpleExpression(token);
        TypeSpec resultType = rootNode != null
                ? rootNode.getTypeSpec()
                : Predefined.undefinedType;

        token = currentToken();
        TokenType tokenType = token.getType();

        // Look for a relational operator.
        if (REL_OPS.contains(tokenType)) {

            // Create a new operator node and adopt the current tree.
            // as it's first child.
            ICodeNodeType nodeType = REL_OPS_MAP.get(tokenType);
            ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
            opNode.addChild(rootNode);

            // consume the operator.
            token = nextToken();

            // Parse the second simple expression. The operator node adopts
            // the simple expression's tree as it's second child.
            ICodeNode simExprNode = parseSimpleExpression(token);
            opNode.addChild(simExprNode);

            // The operator node becomes the new root node.
            rootNode = opNode;

            // Type check: The operands must be comparison compatible.
            TypeSpec simExprType = simExprNode != null
                    ? simExprNode.getTypeSpec()
                    : Predefined.undefinedType;
            if (TypeChecker.areComparisonCompatible(resultType, simExprType)) {
                resultType = Predefined.booleanType;
            } else {
                errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                resultType = Predefined.undefinedType;
            }
        }

        if (rootNode != null) {
            rootNode.setTypeSpec(resultType);
        }

        return rootNode;
    }

    /**
     * Set of additive operators.
     */
    private static final EnumSet<PascalTokenType> ADD_OPS =
            EnumSet.of(PascalTokenType.PLUS,
                    PascalTokenType.MINUS,
                    PascalTokenType.OR);

    /**
     * Map additive operator tokens to node types.
     */
    private static final HashMap<PascalTokenType, ICodeNodeType> ADD_OPS_MAP
            = new HashMap<>();

    static {
        ADD_OPS_MAP.put(PascalTokenType.PLUS, ICodeNodeTypeEnumImpl.ADD);
        ADD_OPS_MAP.put(PascalTokenType.MINUS, ICodeNodeTypeEnumImpl.SUBTRACT);
        ADD_OPS_MAP.put(PascalTokenType.OR, ICodeNodeTypeEnumImpl.OR);
    }

    /**
     * Parse a simple expression.
     *
     * @param token the initial token.
     * @return the root of the generated parse subtree.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseSimpleExpression(Token token) throws Exception {

        Token signToken = null;
        // type of leading sign (if any)
        TokenType signType = null;

        // Look for a leading + or - sign.
        TokenType tokenType = token.getType();
        if ((tokenType == PascalTokenType.PLUS) || (tokenType == PascalTokenType.MINUS)) {
            signType = tokenType;
            signToken = token;
            // consume the + or -
            token = nextToken();
        }

        // Parse a term and make the root of it's tree the root node.
        ICodeNode rootNode = parseTerm(token);
        TypeSpec resultType = rootNode != null
                ? rootNode.getTypeSpec()
                : Predefined.undefinedType;

        // Type check: Leading sign.
        if ((signType != null) && (!TypeChecker.isIntegerOrReal(resultType))) {
            errorHandler.flag(signToken, PascalErrorCode.INCOMPATIBLE_TYPES, this);
        }

        // Was there a leading - sign?
        if (signType == PascalTokenType.MINUS) {

            // Create a NEGATE node and adopt the current tree
            // as its child. The NEGATE node becomes the new root node.
            ICodeNode negateNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.NEGATE);
            negateNode.addChild(rootNode);
            negateNode.setTypeSpec(rootNode.getTypeSpec());
            rootNode = negateNode;
        }

        token = currentToken();
        tokenType = token.getType();

        // Loop over additive operators.
        while (ADD_OPS.contains(tokenType)) {
            TokenType operator = tokenType;

            // Create a new operator node and adopt the current tree
            // as its first child.
            ICodeNodeType nodeType = ADD_OPS_MAP.get(tokenType);
            ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
            opNode.addChild(rootNode);

            // consume the operator
            token = nextToken();

            // Parse another term.  The operator node adopts
            // the term's tree as its second child.
            ICodeNode termNode = parseTerm(token);
            opNode.addChild(termNode);
            TypeSpec termType = termNode != null
                    ? termNode.getTypeSpec()
                    : Predefined.undefinedType;

            // The operator node becomes the new root node.
            rootNode = opNode;

            // Determine the result type.
            switch ((PascalTokenType) operator) {
                case PLUS:
                case MINUS: {
                    // Both operands integer ==> integer result.
                    if (TypeChecker.areBothInteger(resultType, termType)) {
                        resultType = Predefined.integerType;
                    }

                    // Both real operands or one real and one integer operand
                    // ==> real result.
                    else if (TypeChecker.isAtLeastOneReal(resultType,
                            termType)) {
                        resultType = Predefined.realType;
                    } else {
                        errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                    }

                    break;
                }
                case OR: {
                    // Both operands boolean ==> boolean result.
                    if (TypeChecker.areBothBoolean(resultType, termType)) {
                        resultType = Predefined.booleanType;
                    } else {
                        errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                    }
                    break;
                }
                default:
            }

            rootNode.setTypeSpec(resultType);

            token = currentToken();
            tokenType = token.getType();
        }

        return rootNode;
    }

    /**
     * Set of multiplicative operators.
     */
    private static final EnumSet<PascalTokenType> MULT_OPS =
            EnumSet.of(PascalTokenType.STAR,
                    PascalTokenType.SLASH,
                    PascalTokenType.DIV,
                    PascalTokenType.MOD,
                    PascalTokenType.AND);

    /**
     * Map multiplicative operator tokens to node types.
     */
    private static final HashMap<PascalTokenType, ICodeNodeType>
            MULT_OPS_OPS_MAP = new HashMap<>();

    static {
        MULT_OPS_OPS_MAP.put(PascalTokenType.STAR, ICodeNodeTypeEnumImpl.MULTIPLY);
        MULT_OPS_OPS_MAP.put(PascalTokenType.SLASH, ICodeNodeTypeEnumImpl.FLOAT_DIVIDE);
        MULT_OPS_OPS_MAP.put(PascalTokenType.DIV, ICodeNodeTypeEnumImpl.INTEGER_DIVIDE);
        MULT_OPS_OPS_MAP.put(PascalTokenType.MOD, ICodeNodeTypeEnumImpl.MOD);
        MULT_OPS_OPS_MAP.put(PascalTokenType.AND, ICodeNodeTypeEnumImpl.AND);
    }

    /**
     * Parse a term.
     *
     * @param token the initial token.
     * @return the root of the generated parse subtree.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseTerm(Token token) throws Exception {
        // Parse a factor and make its node the root node.
        ICodeNode rootNode = parseFactor(token);
        TypeSpec resultType = rootNode != null
                ? rootNode.getTypeSpec()
                : Predefined.undefinedType;

        token = currentToken();
        TokenType tokenType = token.getType();

        // Loop over multiplicative operators.
        while (MULT_OPS.contains(tokenType)) {
            TokenType operator = tokenType;

            // Create a new operator node and adopt the current tree
            // as its first child.
            ICodeNodeType nodeType = MULT_OPS_OPS_MAP.get(tokenType);
            ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
            opNode.addChild(rootNode);

            // consume the operator
            token = nextToken();

            // Parse another factor.  The operator node adopts
            // the term's tree as its second child.
            ICodeNode factorNode = parseFactor(token);
            opNode.addChild(factorNode);
            TypeSpec factorType = factorNode != null
                    ? factorNode.getTypeSpec()
                    : Predefined.undefinedType;

            // The operator node becomes the new root node.
            rootNode = opNode;

            // Determine the result type.
            switch ((PascalTokenType) operator) {
                case STAR: {
                    // Both operands integer ==> integer result.
                    if (TypeChecker.areBothInteger(resultType, factorType)) {
                        resultType = Predefined.integerType;
                    }

                    // Both real operands or one real and one integer operand
                    // ==> real result.
                    else if (TypeChecker.isAtLeastOneReal(resultType,
                            factorType)) {
                        resultType = Predefined.realType;
                    } else {
                        errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                    }
                    break;
                }
                case SLASH: {
                    // All integer and real operand combinations
                    // ==> real result.
                    if (TypeChecker.areBothInteger(resultType, factorType) ||
                            TypeChecker.isAtLeastOneReal(resultType, factorType)) {
                        resultType = Predefined.realType;
                    } else {
                        errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                    }

                    break;
                }
                case DIV:
                case MOD: {
                    // Both operands integer ==> integer result.
                    if (TypeChecker.areBothInteger(resultType, factorType)) {
                        resultType = Predefined.integerType;
                    } else {
                        errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                    }

                    break;
                }
                case AND: {
                    // Both operands boolean ==> boolean result.
                    if (TypeChecker.areBothBoolean(resultType, factorType)) {
                        resultType = Predefined.booleanType;
                    } else {
                        errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                    }
                    break;
                }
                default:
            }

            rootNode.setTypeSpec(resultType);

            token = currentToken();
            tokenType = token.getType();
        }

        return rootNode;
    }

    /**
     * Parse a factor.
     *
     * @param token the initial token.
     * @return the root of the generated parse subtree.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseFactor(Token token) throws Exception {
        TokenType tokenType = token.getType();
        ICodeNode rootNode = null;

        switch ((PascalTokenType) tokenType) {

            case IDENTIFIER: {
                return parseIdentifier(token);
            }

            case INTEGER: {
                // Create an INTEGER_CONSTANT node as the root node.
                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.INTEGER_CONSTANT);
                rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, token.getValue());

                // consume the number
                token = nextToken();

                rootNode.setTypeSpec(Predefined.integerType);
                break;
            }

            case REAL: {
                // Create an REAL_CONSTANT node as the root node.
                rootNode =
                        ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.REAL_CONSTANT);
                rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, token.getValue());

                // consume the number
                token = nextToken();

                rootNode.setTypeSpec(Predefined.realType);
                break;
            }

            case STRING: {
                String value = (String) token.getValue();

                // Create a STRING_CONSTANT node as the root node.
                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.STRING_CONSTANT);
                rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, value);

                TypeSpec resultType = value.length() == 1
                        ? Predefined.charType
                        : TypeFactory.createStringType(value);

                // consume the string
                token = nextToken();

                rootNode.setTypeSpec(resultType);
                break;
            }

            case NOT: {
                // consume the NOT
                token = nextToken();

                // Create a NOT node as the root node.
                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.NOT);

                // Parse the factor.  The NOT node adopts the
                // factor node as its child.
                ICodeNode factorNode = parseFactor(token);
                rootNode.addChild(factorNode);

                // Type check: The factor must be boolean.
                TypeSpec factorType = factorNode != null
                        ? factorNode.getTypeSpec()
                        : Predefined.undefinedType;
                if (!TypeChecker.isBoolean(factorType)) {
                    errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                }

                rootNode.setTypeSpec(Predefined.booleanType);
                break;
            }

            case LEFT_PAREN: {
                // consume the (
                token = nextToken();

                // Parse an expression and make its node the root node.
                rootNode = parseExpression(token);
                TypeSpec resultType = rootNode != null
                        ? rootNode.getTypeSpec()
                        : Predefined.undefinedType;

                // Look for the matching ) token.
                token = currentToken();
                if (token.getType() == PascalTokenType.RIGHT_PAREN) {
                    // consume the )
                    token = nextToken();
                } else {
                    errorHandler.flag(token, PascalErrorCode.MISSING_RIGHT_PAREN, this);
                }

                rootNode.setTypeSpec(resultType);
                break;
            }

            default: {
                errorHandler.flag(
                        token,
                        PascalErrorCode.UNEXPECTED_TOKEN,
                        this
                );
                break;
            }
        }

        return rootNode;
    }

    /**
     * Parse an identifier.
     *
     * @param token the current token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseIdentifier(Token token) throws Exception {
        ICodeNode rootNode = null;

        // Look up the identifier in the symbol table stack.
        String name = token.getText().toLowerCase();
        SymTabEntry id = symTabStack.lookup(name);

        // Undefined.
        if (id == null) {
            errorHandler.flag(token, PascalErrorCode.IDENTIFIER_UNDEFINED, this);
            id = symTabStack.enterLocal(name);
            id.setDefinition(DefinitionEnumImpl.UNDEFINED);
            id.setTypeSpec(Predefined.undefinedType);
        }

        Definition definitionCode = id.getDefinition();

        switch ((DefinitionEnumImpl) definitionCode) {

            case CONSTANT: {
                Object value = id.getAttribute(SymTabKeyImpl.CONSTANT_VALUE);
                TypeSpec type = id.getTypeSpec();

                if (value instanceof Integer) {
                    rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.INTEGER_CONSTANT);
                    rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, value);
                } else if (value instanceof Float) {
                    rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.REAL_CONSTANT);
                    rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, value);
                } else if (value instanceof String) {
                    rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.STRING_CONSTANT);
                    rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, value);
                }

                id.appendLineNumber(token.getLineNumber());
                // consume the constant identifier
                token = nextToken();

                if (rootNode != null) {
                    rootNode.setTypeSpec(type);
                }

                break;
            }

            case ENUMERATION_CONSTANT: {
                Object value = id.getAttribute(SymTabKeyImpl.CONSTANT_VALUE);
                TypeSpec type = id.getTypeSpec();

                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.INTEGER_CONSTANT);
                rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, value);

                id.appendLineNumber(token.getLineNumber());
                // consume the enum constant identifier
                token = nextToken();

                rootNode.setTypeSpec(type);
                break;
            }

            default: {
                VariableParser variableParser = new VariableParser(this);
                rootNode = variableParser.parse(token, id);
                break;
            }
        }

        return rootNode;
    }
}
