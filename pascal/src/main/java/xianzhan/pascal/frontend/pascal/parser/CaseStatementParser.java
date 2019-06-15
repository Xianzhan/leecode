package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.EofToken;
import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

import java.util.EnumSet;
import java.util.HashSet;

/**
 * Parse a Pascal CASE statement.
 *
 * @author xianzhan
 * @since 2019-06-13
 */
public class CaseStatementParser extends StatementParser {

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public CaseStatementParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set for starting a CASE option constant.
     */
    private static final EnumSet<PascalTokenType> CONSTANT_START_SET =
            EnumSet.of(
                    PascalTokenType.IDENTIFIER,
                    PascalTokenType.INTEGER,
                    PascalTokenType.PLUS,
                    PascalTokenType.MINUS,
                    PascalTokenType.STRING
            );

    /**
     * Synchronization set for OF.
     */
    private static final EnumSet<PascalTokenType> OF_SET =
            CONSTANT_START_SET.clone();

    static {
        OF_SET.add(PascalTokenType.OF);
        OF_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    /**
     * Parse a CASE statement.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        // consume the CASE
        token = nextToken();

        // Create a SELECT node.
        ICodeNode selectNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.SELECT);

        // Parse the CASE expression.
        // The SELECT node adopts the expression subtree as it's first child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        selectNode.addChild(expressionParser.parse(token));

        // Synchronize at the OF.
        token = synchronize(OF_SET);
        if (token.getType() == PascalTokenType.OF) {
            // consume the OF
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_OF, this);
        }

        // Set of CASE branch constants.
        HashSet<Object> constantSet = new HashSet<>();

        // Loop to parse each CASE branch until the END token
        // or the end of the source file.
        while (!(token instanceof EofToken) && (token.getType() != PascalTokenType.END)) {

            // The SELECT node adopts the CASE branch subtree.
            selectNode.addChild(parseBranch(token, constantSet));

            token = currentToken();
            TokenType tokenType = token.getType();

            // Look for the semicolon between CASE branches.
            if (tokenType == PascalTokenType.SEMICOLON) {
                // consume the ;
                token = nextToken();
            } else if (CONSTANT_START_SET.contains(tokenType)) {
                errorHandler.flag(token, PascalErrorCode.MISSING_SEMICOLON, this);
            }
        }

        // Look for the END token.
        if (token.getType() == PascalTokenType.END) {
            // consume END
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_END, this);
        }

        return selectNode;
    }

    /**
     * Parse a CASE branch.
     *
     * @param token       the current token.
     * @param constantSet the set of CASE branch constants.
     * @return the root SELECT_BRANCH node of the subtree.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseBranch(Token token, HashSet<Object> constantSet) throws Exception {

        // Create an SELECT_BRANCH node and a SELECT_CONSTANTS node.
        // The SELECT_BRANCH node adopts the SELECT_CONSTANTS node as it's
        // first child.
        ICodeNode branchNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.SELECT_BRANCH);
        ICodeNode constantsNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.SELECT_CONSTANTS);
        branchNode.addChild(constantsNode);

        // Parse the list of CASE branch constants.
        // The SELECT_CONSTANTS node adopts each constant.
        parseConstantList(token, constantsNode, constantSet);

        // Look for the : token.
        token = currentToken();
        if (token.getType() == PascalTokenType.COLON) {
            // consume the :
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_COLON, this);
        }

        // Parse the CASE branch statement. The SELECT_BRANCH node adopts
        // the statement subtree as it's second child.
        StatementParser statementParser = new StatementParser(this);
        branchNode.addChild(statementParser.parse(token));

        return branchNode;
    }

    /**
     * Synchronization set for COMMA.
     */
    private static final EnumSet<PascalTokenType> COMMA_SET =
            CONSTANT_START_SET.clone();

    static {
        COMMA_SET.add(PascalTokenType.COMMA);
        COMMA_SET.add(PascalTokenType.COLON);
        COMMA_SET.addAll(StatementParser.STMT_START_SET);
        COMMA_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    /**
     * Parse a list of CASE branch constants.
     *
     * @param token         the current token.
     * @param constantsNode the parent SELECT_CONSTANTS node.
     * @param constantSet   the set of CASE branch constants.
     * @throws Exception if an error occurred.
     */
    private void parseConstantList(Token token,
                                   ICodeNode constantsNode,
                                   HashSet<Object> constantSet) throws Exception {
        // Loop to parse each constant.
        while (CONSTANT_START_SET.contains(token.getType())) {

            // The constants list node adopts the constant node.
            constantsNode.addChild(parseConstant(token, constantSet));

            // Synchronize at the comma between constants.
            token = synchronize(COMMA_SET);

            // Look for the comma.
            if (token.getType() == PascalTokenType.COMMA) {
                // consume the ,
                token = nextToken();
            } else if (CONSTANT_START_SET.contains(token.getType())) {
                errorHandler.flag(token, PascalErrorCode.MISSING_COMMA, this);
            }
        }
    }

    /**
     * Parse CASE branch constant.
     *
     * @param token       the current token.
     * @param constantSet the set of CASE branch constants.
     * @return the constant node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseConstant(Token token, HashSet<Object> constantSet) throws Exception {
        TokenType sign = null;
        ICodeNode constantNode = null;

        // Synchronize at the start of a constant.
        token = synchronize(CONSTANT_START_SET);
        TokenType tokenType = token.getType();

        // Plus or minus sign?
        if (tokenType == PascalTokenType.PLUS || tokenType == PascalTokenType.MINUS) {
            sign = tokenType;
            // consume sign
            token = nextToken();
        }

        // Parse the constant.
        switch ((PascalTokenType) token.getType()) {

            case IDENTIFIER: {
                constantNode = parseIdentifierConstant(token, sign);
                break;
            }

            case INTEGER: {
                constantNode = parseIntegerConstant(token.getText(), sign);
                break;
            }

            case STRING: {
                constantNode = parseCharacterConstant(token,
                                                      (String) token.getValue(),
                                                      sign);
                break;
            }

            default: {
                errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
                break;
            }
        }

        // Check for reused constants.
        if (constantNode != null) {
            Object value = constantNode.getAttribute(ICodeKeyEnumImpl.VALUE);

            if (constantSet.contains(value)) {
                errorHandler.flag(token,
                                  PascalErrorCode.CASE_CONSTANT_REUSED,
                                  this);
            } else {
                constantSet.add(value);
            }
        }

        // consume the constant
        nextToken();
        return constantNode;
    }

    /**
     * Parse an identifier CASE constant.
     *
     * @param token the current token value string.
     * @param sign  the sign, if any.
     * @return the constant node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseIdentifierConstant(Token token, TokenType sign) throws Exception {
        // Placeholder: Don't allow for now.
        errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
        return null;
    }

    /**
     * Parse an integer CASE constant.
     *
     * @param value the current token value string.
     * @param sign  the sign, if any.
     * @return the constant node.
     */
    private ICodeNode parseIntegerConstant(String value, TokenType sign) {
        ICodeNode constantNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.INTEGER_CONSTANT);
        int intValue = Integer.parseInt(value);

        if (sign == PascalTokenType.MINUS) {
            intValue = -intValue;
        }

        constantNode.setAttribute(ICodeKeyEnumImpl.VALUE, intValue);
        return constantNode;
    }

    private ICodeNode parseCharacterConstant(Token token, String value,
                                             TokenType sign) {
        ICodeNode constantNode = null;

        if (sign != null) {
            errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
        } else {
            if (value.length() == 1) {
                constantNode =
                        ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.STRING_CONSTANT);
                constantNode.setAttribute(ICodeKeyEnumImpl.VALUE, value);
            }
        }
        return constantNode;
    }
}
