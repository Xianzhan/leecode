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
 * Parse a Pascal WHILE statement.
 *
 * @author xianzhan
 * @since 2019-06-15
 */
public class WhileStatementParser extends StatementParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public WhileStatementParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set for DO.
     */
    private static final EnumSet<PascalTokenType> DO_SET =
            StatementParser.STMT_START_SET.clone();

    static {
        DO_SET.add(PascalTokenType.DO);
        DO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    /**
     * Parse a WHILE statement.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        // consume the WHILE
        token = nextToken();

        // Create LOOP, TEST, and NOT nodes.
        ICodeNode loopNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.LOOP);
        ICodeNode breakNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.TEST);
        ICodeNode notNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.NOT);

        // The LOOP node adopts the TEST node as its first child.
        // The TEST node adopts the NOT node as its only child.
        loopNode.addChild(breakNode);
        breakNode.addChild(notNode);

        // Parse the expression.
        // The NOT node adopts the expression subtree as its only child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        notNode.addChild(expressionParser.parse(token));

        // Synchronize at the DO.
        token = synchronize(DO_SET);
        if (token.getType() == PascalTokenType.DO) {
            // consume the DO
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_DO, this);
        }

        // Parse the statement.
        // The LOOP node adopts the statement subtree as its second child.
        StatementParser statementParser = new StatementParser(this);
        loopNode.addChild(statementParser.parse(token));

        return loopNode;
    }
}
