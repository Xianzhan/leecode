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
     * Synchronization set for starting a statement.
     */
    protected static final EnumSet<PascalTokenType> STMT_START_SET =
            EnumSet.of(
                    PascalTokenType.BEGIN,
                    PascalTokenType.CASE,
                    PascalTokenType.FOR,
                    PascalTokenType.IF,
                    PascalTokenType.REPEAT,
                    PascalTokenType.WHILE,
                    PascalTokenType.IDENTIFIER,
                    PascalTokenType.SEMICOLON
            );

    /**
     * Synchronization set for following a statement.
     */
    protected static final EnumSet<PascalTokenType> STMT_FOLLOW_SET =
            EnumSet.of(
                    PascalTokenType.SEMICOLON,
                    PascalTokenType.END,
                    PascalTokenType.ELSE,
                    PascalTokenType.UNTIL,
                    PascalTokenType.DOT
            );

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
                    AssignmentStatementParser assignmentStatementParser =
                            new AssignmentStatementParser(this);
                    statementNode = assignmentStatementParser.parse(token);
                    break;
                }

                case REPEAT: {
                    RepeatStatementParser repeatParser =
                            new RepeatStatementParser(this);
                    statementNode = repeatParser.parse(token);
                    break;
                }

                case WHILE: {
                    WhileStatementParser whileParser =
                            new WhileStatementParser(this);
                    statementNode = whileParser.parse(token);
                    break;
                }

                case FOR: {
                    ForStatementParser forParser = new ForStatementParser(this);
                    statementNode = forParser.parse(token);
                    break;
                }

                case IF: {
                    IfStatementParser ifParser = new IfStatementParser(this);
                    statementNode = ifParser.parse(token);
                    break;
                }

                case CASE: {
                    CaseStatementParser caseParser = new CaseStatementParser(this);
                    statementNode = caseParser.parse(token);
                    break;
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
        // Synchronization set for the terminator.
        EnumSet<PascalTokenType> terminatorSet = STMT_START_SET.clone();
        terminatorSet.add(terminator);

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
            else if (STMT_START_SET.contains(tokenType)) {
                errorHandler.flag(token, PascalErrorCode.MISSING_SEMICOLON, this);
            }

            // Synchronize at the start of the next statement
            // or at the terminator.
            token = synchronize(terminatorSet);
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
