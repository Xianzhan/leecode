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

/**
 * Parse a Pascal statement.
 *
 * @author xianzhan
 * @since 2019-06-03
 */
public class StatementParser extends PascalParserTD {

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public StatementParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Parse a statement.
     * To be overridden by the specialized statement parser subclasses.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    public ICodeNode parse(Token token) throws Exception {
        ICodeNode statementNode = null;

        TokenType tokenType = token.getType();
        if (tokenType instanceof PascalTokenType) {
            PascalTokenType pascalTokenType = (PascalTokenType) tokenType;
            switch (pascalTokenType) {
                case BEGIN: {
                    CompoundStatementParser compoundStatementParser =
                            new CompoundStatementParser(this);
                    statementNode = compoundStatementParser.parse(token);
                    break;
                }

                // An assignment statement begins with a variable's identifier.
                case IDENTIFIER: {

                }

                default: {
                    statementNode =
                            ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.NO_OP);
                    break;
                }
            }

            // Set the current line number as an attribute.
            setLineNumber(statementNode, token);
        }
        return statementNode;
    }

    /**
     * Set the current line number as a statement node attribute.
     *
     * @param node  ICodeNode
     * @param token Token
     */
    protected void setLineNumber(ICodeNode node, Token token) {
        if (node != null) {
            node.setAttribute(ICodeKeyEnumImpl.LINE, token.getLineNumber());
        }
    }

    /**
     * Parse a statement list
     *
     * @param token      the current token.
     * @param parentNode the parent node of the statement list.
     * @param terminator the token type of the node that terminator the list.
     * @param errorCode  the error code if the terminator token is missing.
     * @throws Exception if an error occurred.
     */
    protected void parseList(Token token,
                             ICodeNode parentNode,
                             PascalTokenType terminator,
                             PascalErrorCode errorCode) throws Exception {
        // Loop to parse each statement until the END token
        // or the end of the source file.
        while (!(token instanceof EofToken) && (token.getType() != terminator)) {
            // Parse a statement. The parent node adopts the statement node.
            ICodeNode statementNode = parse(token);
            parentNode.addChild(statementNode);

            token = currentToken();
            TokenType tokenType = token.getType();

            // Look for the semicolon between statements.
            if (tokenType == PascalTokenType.SEMICOLON) {
                // consume the ;
                token = nextToken();
            }

            // If at the start of the next assignment statement
            // then missing a semicolon.
            else if (tokenType == PascalTokenType.IDENTIFIER) {
                errorHandler.flag(token, PascalErrorCode.MISSING_SEMICOLON, this);
            }

            // Unexpected token.
            else if (tokenType != terminator) {
                errorHandler.flag(token, PascalErrorCode.UNEXPECTED_TOKEN, this);
                token = nextToken();
            }
        }

        // Look for the terminator token.
        if (token.getType() == terminator) {
            // consume the terminator token
            token = nextToken();
        } else {
            errorHandler.flag(token, errorCode, this);
        }
    }
}
