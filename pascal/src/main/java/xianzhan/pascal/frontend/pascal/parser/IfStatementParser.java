package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

import java.util.EnumSet;

/**
 * Parse a Pascal IF statement.
 *
 * @author xianzhan
 * @since 2019-06-15
 */
public class IfStatementParser extends StatementParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public IfStatementParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set for THEN.
     */
    private static final EnumSet<PascalTokenType> THEN_SET =
            StatementParser.STMT_START_SET.clone();

    static {
        THEN_SET.add(PascalTokenType.THEN);
        THEN_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    /**
     * Parse an IF statement.
     *
     * <pre>
     * IF (i = j) THEN t := 200
     *            ELSE f := -200;
     * </pre>
     *
     * <pre>
     *                     IF
     *         /           |                 \
     *        EQ         ASSIGN             ASSIGN
     *     /    \        /     \           /     \
     *   VAR:i VAR:j  VAR:t CONSTANT:200 VAR:f  NEGATE
     *                                            |
     *                                        CONSTANT:200
     * </pre>
     * <p>
     * The  IF node has either two or three children. The first child is the relational expression subtree and the second child is the subtree
     * of the  THEN nested statement. If there is an  ELSE part, the third child is the subtree of the  ELSE nested statement. Otherwise, the  IF node
     * has only two children.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        // consume the IF
        token = nextToken();

        // Create an IF node.
        ICodeNode ifNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.IF);

        // Parse the expression.
        // The IF node adopts the expression subtree as its first child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        ifNode.addChild(expressionParser.parse(token));

        // Synchronize at the THEN.
        token = synchronize(THEN_SET);
        if (token.getType() == PascalTokenType.THEN) {
            // consume the THEN
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_THEN, this);
        }

        // Parse the THEN statement.
        // The IF node adopts the statement subtree as its second child.
        StatementParser statementParser = new StatementParser(this);
        ifNode.addChild(statementParser.parse(token));
        token = currentToken();

        // Look for an ELSE.
        if (token.getType() == PascalTokenType.ELSE) {
            // consume the THEN
            token = nextToken();

            // Parse the ELSE statement.
            // The IF node adopts the statement subtree as its third child.
            ifNode.addChild(statementParser.parse(token));
        }

        return ifNode;
    }
}
