package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.TypeChecker;

import java.util.EnumSet;

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

    private static final EnumSet<PascalTokenType> COLON_EQUALS_SET =
            ExpressionParser.EXPR_START_SET.clone();

    static {
        COLON_EQUALS_SET.add(PascalTokenType.COLON_EQUALS);
        COLON_EQUALS_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    /**
     * Parse an assignment statement.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        // Create the ASSIGN node.
        ICodeNode assignNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.ASSIGN);

        // Parse the target variable.
        VariableParser variableParser = new VariableParser(this);
        ICodeNode targetNode = variableParser.parse(token);
        TypeSpec targetType = targetNode != null
                ? targetNode.getTypeSpec()
                : Predefined.undefinedType;

        // The ASSIGN node adopts the variable node as it's first child.
        assignNode.addChild(targetNode);

        // Synchronize no the := token.
        token = synchronize(COLON_EQUALS_SET);
        if (token.getType() == PascalTokenType.COLON_EQUALS) {
            // consume the :=
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_COLON_EQUALS, this);
        }

        // Parse the expression.  The ASSIGN node adopts the expression's
        // node as its second child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        ICodeNode exprNode = expressionParser.parse(token);
        assignNode.addChild(exprNode);

        // Type check: Assignment compatible?
        TypeSpec exprType = exprNode != null ? exprNode.getTypeSpec()
                : Predefined.undefinedType;
        if (!TypeChecker.areAssignmentCompatible(targetType, exprType)) {
            errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
        }

        assignNode.setTypeSpec(targetType);
        return assignNode;
    }
}
