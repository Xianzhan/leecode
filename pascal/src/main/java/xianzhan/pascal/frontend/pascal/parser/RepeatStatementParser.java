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
