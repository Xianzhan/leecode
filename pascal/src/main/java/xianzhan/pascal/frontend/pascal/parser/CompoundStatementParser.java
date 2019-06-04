package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

/**
 * Parse a Pascal compound statement.
 * <p>
 * -> BEGIN -> statement list -> END ->
 *
 * @author xianzhan
 * @since 2019-06-03
 */
public class CompoundStatementParser extends StatementParser {

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public CompoundStatementParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Parse a compound statement.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        // consume the BEGIN
        token = nextToken();

        // Create the COMPOUND node.
        ICodeNode compoundNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.COMPOUND);

        // Parse the statement list terminated by the END token.
        StatementParser statementParser = new StatementParser(this);
        statementParser.parseList(token,
                                  compoundNode,
                                  PascalTokenType.END,
                                  PascalErrorCode.MISSING_END);

        return compoundNode;
    }
}
