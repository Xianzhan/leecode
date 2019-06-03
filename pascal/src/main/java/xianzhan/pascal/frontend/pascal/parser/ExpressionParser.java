package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.ICodeNodeType;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

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
    private static final HashMap<PascalTokenType, ICodeNodeType> REL_OPS_MAP
            = new HashMap<>();

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
            opNode.addChild(parseSimpleExpression(token));

            // The operator node becomes the new root node.
            rootNode = opNode;
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

        // type of leading sign (if any)
        TokenType signType = null;

        // Look for a leading + or - sign.
        TokenType tokenType = token.getType();
        if ((tokenType == PascalTokenType.PLUS) || (tokenType == PascalTokenType.MINUS)) {
            signType = tokenType;
            // consume the + or -
            token = nextToken();
        }

        // Parse a term and make the root of it's tree the root node.
        ICodeNode rootNode = parseTerm(token);

        // Was there a leading - sign?
        if (signType == PascalTokenType.MINUS) {

            // Create a NEGATE node and adopt the current tree
            // as its child. The NEGATE node becomes the new root node.
            ICodeNode negateNode =
                    ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.NEGATE);
            negateNode.addChild(rootNode);
            rootNode = negateNode;
        }

        token = currentToken();
        tokenType = token.getType();

        // Loop over additive operators.
        while (ADD_OPS.contains(tokenType)) {

            // Create a new operator node and adopt the current tree
            // as its first child.
            ICodeNodeType nodeType = ADD_OPS_MAP.get(tokenType);
            ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
            opNode.addChild(rootNode);

            token = nextToken();  // consume the operator

            // Parse another term.  The operator node adopts
            // the term's tree as its second child.
            opNode.addChild(parseTerm(token));

            // The operator node becomes the new root node.
            rootNode = opNode;

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

        token = currentToken();
        TokenType tokenType = token.getType();

        // Loop over multiplicative operators.
        while (MULT_OPS.contains(tokenType)) {

            // Create a new operator node and adopt the current tree
            // as its first child.
            ICodeNodeType nodeType = MULT_OPS_OPS_MAP.get(tokenType);
            ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
            opNode.addChild(rootNode);

            // consume the operator
            token = nextToken();

            // Parse another factor.  The operator node adopts
            // the term's tree as its second child.
            opNode.addChild(parseFactor(token));

            // The operator node becomes the new root node.
            rootNode = opNode;

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
                // Look up the identifier in the symbol table stack.
                // Flag the identifier as undefined if it's not found.
                String name = token.getText().toLowerCase();
                SymTabEntry id = symTabStack.lookup(name);
                if (id == null) {
                    errorHandler.flag(token, PascalErrorCode.IDENTIFIER_UNDEFINED, this);
                    id = symTabStack.enterLocal(name);
                }

                rootNode =
                        ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.VARIABLE);
                rootNode.setAttribute(ICodeKeyEnumImpl.ID, id);
                id.appendLineNumber(token.getLineNumber());

                // consume the identifier
                token = nextToken();
                break;
            }

            case INTEGER: {
                // Create an INTEGER_CONSTANT node as the root node.
                rootNode =
                        ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.INTEGER_CONSTANT);
                rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, token.getValue());

                // consume the number
                token = nextToken();
                break;
            }

            case REAL: {
                // Create an REAL_CONSTANT node as the root node.
                rootNode =
                        ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.REAL_CONSTANT);
                rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, token.getValue());

                // consume the number
                token = nextToken();
                break;
            }

            case STRING: {
                String value = (String) token.getValue();

                // Create a STRING_CONSTANT node as the root node.
                rootNode =
                        ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.STRING_CONSTANT);
                rootNode.setAttribute(ICodeKeyEnumImpl.VALUE, value);

                // consume the string
                token = nextToken();
                break;
            }

            case NOT: {
                // consume the NOT
                token = nextToken();

                // Create a NOT node as the root node.
                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.NOT);

                // Parse the factor.  The NOT node adopts the
                // factor node as its child.
                rootNode.addChild(parseFactor(token));

                break;
            }

            case LEFT_PAREN: {
                // consume the (
                token = nextToken();

                // Parse an expression and make its node the root node.
                rootNode = parseExpression(token);

                // Look for the matching ) token.
                token = currentToken();
                if (token.getType() == PascalTokenType.RIGHT_PAREN) {
                    // consume the )
                    token = nextToken();
                } else {
                    errorHandler.flag(token, PascalErrorCode.MISSING_RIGHT_PAREN, this);
                }

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
}
