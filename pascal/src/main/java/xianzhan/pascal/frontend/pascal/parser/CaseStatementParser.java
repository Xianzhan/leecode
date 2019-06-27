package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.EofToken;
import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.intermediate.impl.TypeChecker;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;

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
     * <p>
     * The  CASE statement is the most challenging of the Pascal control statements to parse. The statement parser subclass
     * CompoundStatementParser parses the  CASE statement and generates its parse tree. For the statement
     *
     * <pre>
     * CASE i+1 OF
     *     1:       j := i;
     *     4:       j := 4*i;
     *     5, 2, 3: j := 523*i;
     * END
     * </pre>
     *
     * <pre>
     *               SELECT
     *      /            |
     *     ADD           ├ - - - - - - - - - > SELECT
     *    /   \          |                     BRANCH
     * VAR:i CONSTANT:1  |        /                 |
     *                   |    SELECT              ASSIGN
     *                   |  CONSTANTS             /     \
     *                   |      |               VAR:j  VAR:i
     *                   |   CONSTANT:1
     *                   ├ - - - - - - - - - > SELECT
     *                   |                     BRANCH
     *                   |        /                 |
     *                   |    SELECT              ASSIGN
     *                   |  CONSTANTS             /     \
     *                   |      |               VAR:j MULTIPLY
     *                   |   CONSTANT:4               /       \
     *                   |                       CONSTANT:4  VAR:i
     *                   └ - - - - - - - - - > SELECT
     *                                         BRANCH
     *                                 /                 \
     *                            SELECT               ASSIGN
     *                          CONSTANTS             /      \
     *                 /        |       \           VAR:j MULTIPLY
     *          CONSTANT:5 CONSTANT:2 CONSTANT:3          /      \
     *                                             CONSTANT:523 VAR:i
     * </pre>
     * <p>
     * The synchronization set  CONSTANT_START_SET contains the token types that can start a constant. Method  parse() creates a  SELECT
     * node, and after parsing the  CASE expression, it uses the synchronization set  OF_SET to synchronize itself at the  OF token. The  while loop
     * calls  parseBranch() to parse each  CASE branch and then looks for a semicolon. The loop exits with the  END token. The method returns
     * the  SELECT node.
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
        ICodeNode selectNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.SELECT);

        // Parse the CASE expression.
        // The SELECT node adopts the expression subtree as it's first child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        ICodeNode exprNode = expressionParser.parse(token);
        selectNode.addChild(exprNode);

        // Type check: The CASE expression's type must be integer, character,
        //             or enumeration.
        TypeSpec exprType = exprNode != null
                ? exprNode.getTypeSpec()
                : Predefined.undefinedType;
        if (!TypeChecker.isInteger(exprType) &&
                !TypeChecker.isChar(exprType) &&
                (exprType.getForm() != TypeFormEnumImpl.ENUMERATION)) {
            errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
        }

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
            selectNode.addChild(parseBranch(token, exprType, constantSet));

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
     * <p>
     * Method  parseBranch() creates a  SELECT_BRANCH node and a  SELECT_CONSTANTS node before calling  parseConstantList() to parse
     * the list of constants. It then looks for a colon. The  SELECT_CONSTANTS node adopts the constant nodes and the  SELECT_BRANCH node
     * adopts the  SELECT_CONSTANTS node. A call to  statementParser.parse() parses the branch statement, and the  SELECT_BRANCH node
     * adopts the root of the branch statement’s parse tree as its second child.
     *
     * @param token       the current token.
     * @param exprType    the CASE expression type.
     * @param constantSet the set of CASE branch constants.
     * @return the root SELECT_BRANCH node of the subtree.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseBranch(Token token, TypeSpec exprType, HashSet<Object> constantSet) throws Exception {

        // Create an SELECT_BRANCH node and a SELECT_CONSTANTS node.
        // The SELECT_BRANCH node adopts the SELECT_CONSTANTS node as it's
        // first child.
        ICodeNode branchNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.SELECT_BRANCH);
        ICodeNode constantsNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.SELECT_CONSTANTS);
        branchNode.addChild(constantsNode);

        // Parse the list of CASE branch constants.
        // The SELECT_CONSTANTS node adopts each constant.
        parseConstantList(token, exprType, constantsNode, constantSet);

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
     * <p>
     * Method  parseConstantList() parses a list of constants separated by commas. It calls  parseConstant() to parse each constant. In
     * this chapter,  parseConstant() can parse integer constants (optionally preceded by a  + or  – sign) by calling  parseIntegerConstant()
     * and character constants by calling  parseCharacterConstant().
     *
     * @param token         the current token.
     * @param exprType      the CASE expression type.
     * @param constantsNode the parent SELECT_CONSTANTS node.
     * @param constantSet   the set of CASE branch constants.
     * @throws Exception if an error occurred.
     */
    private void parseConstantList(Token token,
                                   TypeSpec exprType,
                                   ICodeNode constantsNode,
                                   HashSet<Object> constantSet) throws Exception {
        // Loop to parse each constant.
        while (CONSTANT_START_SET.contains(token.getType())) {

            // The constants list node adopts the constant node.
            constantsNode.addChild(parseConstant(token, exprType, constantSet));

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
     * <p>
     * The syntax diagram for the  CASE statement in Figure 7-1 does not reflect two syntax rules regarding a branch constant:
     * <ul>
     * <li>
     * A branch constant can be an integer or a single character.
     * </li>
     * <li>
     * A branch constant cannot be used more than once in a  CASE statement.
     * </li>
     * </ul>
     * To address the first rule, method  parseConstant() checks that any STRING token has a value of length 1. For the second rule,
     * method  parse() creates the set  constantSet , which is passed down to method  parseConstant() . When it is done parsing the constant,
     * method  parseConstant() checks the set to ensure that the constant has not already been used.
     *
     * @param token       the current token.
     * @param exprType    the CASE expression type.
     * @param constantSet the set of CASE branch constants.
     * @return the constant node.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseConstant(Token token, TypeSpec exprType, HashSet<Object> constantSet) throws Exception {
        TokenType sign = null;
        ICodeNode constantNode = null;
        TypeSpec constantType = null;

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
                if (constantNode != null) {
                    constantType = constantNode.getTypeSpec();
                }
                break;
            }

            case INTEGER: {
                constantNode = parseIntegerConstant(token.getText(), sign);
                constantType = Predefined.integerType;
                break;
            }

            case STRING: {
                constantNode = parseCharacterConstant(token, (String) token.getValue(), sign);
                constantType = Predefined.charType;
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
                errorHandler.flag(token, PascalErrorCode.CASE_CONSTANT_REUSED, this);
            } else {
                constantSet.add(value);
            }
        }

        // Type check: The constant type must be comparison compatible
        //             with the CASE expression type.
        if (!TypeChecker.areComparisonCompatible(exprType, constantType)) {
            errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
        }

        // consume the constant
        nextToken();

        constantNode.setTypeSpec(constantType);
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
        ICodeNode constantNode = null;
        TypeSpec constantType = null;

        // Look up the identifier in the symbol table stack.
        String name = token.getText().toLowerCase();
        SymTabEntry id = symTabStack.lookup(name);

        // Undefined.
        if (id == null) {
            id = symTabStack.enterLocal(name);
            id.setDefinition(DefinitionEnumImpl.UNDEFINED);
            id.setTypeSpec(Predefined.undefinedType);
            errorHandler.flag(token, PascalErrorCode.IDENTIFIER_UNDEFINED, this);
            return null;
        }

        Definition definitionCode = id.getDefinition();

        // Constant identifier.
        if ((definitionCode == DefinitionEnumImpl.CONSTANT) || (definitionCode == DefinitionEnumImpl.ENUMERATION_CONSTANT)) {
            Object constantValue = id.getAttribute(SymTabKeyImpl.CONSTANT_VALUE);
            constantType = id.getTypeSpec();

            // Type check: Leading sign permitted only for integer constants.
            if ((sign != null) && !TypeChecker.isInteger(constantType)) {
                errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
            }

            constantNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.INTEGER_CONSTANT);
            constantNode.setAttribute(ICodeKeyEnumImpl.VALUE, constantValue);
        }

        id.appendLineNumber(token.getLineNumber());

        if (constantNode != null) {
            constantNode.setTypeSpec(constantType);
        }

        return constantNode;
    }

    /**
     * Parse an integer CASE constant.
     *
     * @param value the current token value string.
     * @param sign  the sign, if any.
     * @return the constant node.
     */
    private ICodeNode parseIntegerConstant(String value, TokenType sign) {
        ICodeNode constantNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.INTEGER_CONSTANT);
        int intValue = Integer.parseInt(value);

        if (sign == PascalTokenType.MINUS) {
            intValue = -intValue;
        }

        constantNode.setAttribute(ICodeKeyEnumImpl.VALUE, intValue);
        return constantNode;
    }

    private ICodeNode parseCharacterConstant(Token token, String value, TokenType sign) {
        ICodeNode constantNode = null;

        if (sign != null) {
            errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
        } else {
            if (value.length() == 1) {
                constantNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.STRING_CONSTANT);
                constantNode.setAttribute(ICodeKeyEnumImpl.VALUE, value);
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
            }
        }
        return constantNode;
    }
}
