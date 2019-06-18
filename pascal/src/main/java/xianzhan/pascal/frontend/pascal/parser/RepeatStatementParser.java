package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

/**
 * Parse a Pascal REPEAT statement.
 *
 * @author xianzhan
 * @since 2019-06-15
 */
public class RepeatStatementParser extends StatementParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public RepeatStatementParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Parse a REPEAT statement.
     *
     * <pre>
     * REPEAT
     *     j := i;
     *     k := i
     * UNTIL i <= j
     * </pre>
     *
     * <pre>
     *                 LOOP
     *          /        |       \
     *       ASSIGN    ASSIGN    TEST
     *     /   \       /   \       |
     * VAR:j  VAR:i  VAR:k VAR:i  LE
     *                            / \
     *                         VAR:i VAR:j
     * </pre>
     * <p>
     * The  LOOP node can have any number of children that are statement subtrees. At least one child should be a  TEST node whose only
     * child is a relational expression subtree. At runtime, the loop exits if the expression evaluates to true. A  TEST node can be any one of
     * the  LOOP node’s children, so the exit test can occur at the start of the loop, at the end of the loop, or somewhere in between. For a
     * Pascal  REPEAT statement, the  TEST node is the  LOOP node’s last child and therefore, the exit test is at the end of the loop.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        // consume the REPEAT
        token = nextToken();

        // Create the LOOP and TEST nodes.
        ICodeNode loopNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.LOOP);
        ICodeNode testNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.TEST);

        // Parse the statement list terminated by the UNTIL token.
        // The LOOP node is the parent of the statement subtrees.
        StatementParser statementParser = new StatementParser(this);
        statementParser.parseList(token,
                loopNode,
                PascalTokenType.UNTIL,
                PascalErrorCode.MISSING_UNTIL);
        token = currentToken();

        // Parse the expression.
        // The TEST node adopts the expression subtree as its only child.
        // The LOOP node adopts the TEST node.
        ExpressionParser expressionParser = new ExpressionParser(this);
        testNode.addChild(expressionParser.parse(token));
        loopNode.addChild(testNode);

        return loopNode;
    }
}
