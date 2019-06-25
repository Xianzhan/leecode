package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

/**
 * Parse a Pascal block.
 *
 * @author xianzhan
 * @since 2019-06-23
 */
public class BlockParser extends PascalParserTD {

    public BlockParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Parse a block.
     *
     * @param token     the initial token.
     * @param routineId the symbol table entry of the routine name.
     * @return the root node of the parse tree.
     * @throws Exception if an error occurred.
     */
    public ICodeNode parse(Token token, SymTabEntry routineId) throws Exception {
        DeclarationsParser declarationsParser = new DeclarationsParser(this);
        StatementParser statementParser = new StatementParser(this);

        // Parse any declarations.
        declarationsParser.parse(token);

        token = synchronize(StatementParser.STMT_START_SET);
        TokenType tokenType = token.getType();
        ICodeNode rootNode = null;

        // Look for the BEGIN token to parse a compound statement.
        if (tokenType == PascalTokenType.BEGIN) {
            rootNode = statementParser.parse(token);
        }

        // Missing BEGIN: Attempt to parse anyway if possible.
        else {
            errorHandler.flag(token, PascalErrorCode.MISSING_BEGIN, this);

            if (StatementParser.STMT_START_SET.contains(tokenType)) {
                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.COMPOUND);
                statementParser.parseList(token, rootNode, PascalTokenType.END, PascalErrorCode.MISSING_END);
            }
        }

        return rootNode;
    }
}
