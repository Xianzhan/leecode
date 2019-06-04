package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

/**
 * Parse a Pascal assignment statement.
 * <p>
 * -> variable -> := -> expression ->
 *
 * @author xianzhan
 * @since 2019-06-03
 */
public class AssignmentStatementParser extends StatementParser {

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public AssignmentStatementParser(PascalParserTD parent) {
        super(parent);
    }

    @Override
    public ICodeNode parse(Token token) throws Exception {
        // Create the ASSIGN node.
        ICodeNode assignNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.ASSIGN);

        // Look up the target identifier in the symbol table stack.
        // Enter the identifier into the table if it's not found.
        String targetName = token.getText().toLowerCase();
        SymTabEntry targetId = symTabStack.lookup(targetName);
        if (targetId == null) {
            targetId = symTabStack.enterLocal(targetName);
        }
        targetId.appendLineNumber(token.getLineNumber());

        // consume the identifier token
        token = nextToken();

        // Create the variable node and set it's name attribute.
        ICodeNode variableNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.VARIABLE);
        variableNode.setAttribute(ICodeKeyEnumImpl.ID, targetId);

        // The ASSIGN node adopts the variable node as it's first child.
        assignNode.addChild(variableNode);

        // Look for the := token.
        if (token.getType() == PascalTokenType.COLON_EQUALS) {
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_COLON_EQUALS, this);
        }

        // Parse the expression. The ASSIGN node adopts the expression's
        // node as it's second child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        assignNode.addChild(expressionParser.parse(token));

        return assignNode;
    }
}
