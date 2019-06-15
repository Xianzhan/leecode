package xianzhan.pascal.frontend.pascal.parser;

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
 * Parse a FOR statement.
 *
 * @author xianzhan
 * @since 2019-06-15
 */
public class ForStatementParser extends StatementParser {

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public ForStatementParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set for TO or DOWNTO.
     */
    private static final EnumSet<PascalTokenType> TO_DOWNTO_SET =
            ExpressionParser.EXPR_START_SET.clone();

    static {
        TO_DOWNTO_SET.add(PascalTokenType.TO);
        TO_DOWNTO_SET.add(PascalTokenType.DOWNTO);
        TO_DOWNTO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
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
     * Parse the FOR statement.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        // consume the FOR
        token = nextToken();
        Token targetToken = token;

        // Create the loop COMPOUND, LOOP, and TEST nodes.
        ICodeNode compoundNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.COMPOUND);
        ICodeNode loopNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.LOOP);
        ICodeNode testNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.TEST);

        // Parse the embedded initial assignment.
        AssignmentStatementParser assignmentParser =
                new AssignmentStatementParser(this);
        ICodeNode initAssignNode = assignmentParser.parse(token);

        // Set the current line number attribute.
        setLineNumber(initAssignNode, targetToken);

        // The COMPOUND node adopts the initial ASSIGN and the LOOP nodes
        // as it's first and second children.
        compoundNode.addChild(initAssignNode);
        compoundNode.addChild(loopNode);

        // Synchronize at the TO or DOWNTO.
        token = synchronize(TO_DOWNTO_SET);
        TokenType direction = token.getType();

        // Look for the TO or DOWNTO.
        if ((direction == PascalTokenType.TO) || (direction == PascalTokenType.DOWNTO)) {
            // consume the TO or DOWNTO
            token = nextToken();
        } else {
            direction = PascalTokenType.TO;
            errorHandler.flag(token, PascalErrorCode.MISSING_TO_DOWNTO, this);
        }

        // Create a relational operator node: GT for TO, or LT for DOWNTO.
        ICodeNode relOpNode =
                ICodeFactory.createICodeNode(direction == PascalTokenType.TO
                                                     ? ICodeNodeTypeEnumImpl.GT
                                                     : ICodeNodeTypeEnumImpl.LT);

        // Copy the control VARIABLE node. The relational operator
        // node adopts the copied VARIABLE node as it's first child.
        ICodeNode controlVarNode = initAssignNode.getChildren().get(0);
        relOpNode.addChild(controlVarNode.copy());

        // Parse the termination expression. The relational operator node
        // adopts the expression as it's second child.
        ExpressionParser expressionParser = new ExpressionParser(this);
        relOpNode.addChild(expressionParser.parse(token));

        // The TEST node adopts the relational operator node as it's only child.
        // The LOOP node adopts the TEST node as it's first child.
        testNode.addChild(relOpNode);
        loopNode.addChild(testNode);

        // Synchronize at the DO.
        token = synchronize(DO_SET);
        if (token.getType() == PascalTokenType.DO) {
            // consume the DO
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_DO, this);
        }

        // Parse the nested statement. The LOOP node adopts the statement
        // node as it's second child.
        StatementParser statementParser = new StatementParser(this);
        loopNode.addChild(statementParser.parse(token));

        // Create an assignment with a copy of the control variable
        // to advance the value of the variable.
        ICodeNode nextAssignNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.ASSIGN);
        nextAssignNode.addChild(controlVarNode.copy());

        // Create the arithmetic operator node:
        // ADD for TO, or SUBTRACT for DOWNTO.
        ICodeNode arithOpNode = ICodeFactory.createICodeNode(
                direction == PascalTokenType.TO
                        ? ICodeNodeTypeEnumImpl.ADD
                        : ICodeNodeTypeEnumImpl.SUBTRACT
        );

        // The operator node adopts a copy of the loop variable as its
        // first child and the value 1 as its second child.
        arithOpNode.addChild(controlVarNode.copy());
        ICodeNode oneNode =
                ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.INTEGER_CONSTANT);
        oneNode.setAttribute(ICodeKeyEnumImpl.VALUE, 1);
        arithOpNode.addChild(oneNode);

        // The next ASSIGN node adopts the arithmetic operator node as its
        // second child. The loop node adopts the next ASSIGN node as its
        // third child.
        nextAssignNode.addChild(arithOpNode);
        loopNode.addChild(nextAssignNode);

        // Set the current line number attribute.
        setLineNumber(nextAssignNode, targetToken);

        return compoundNode;
    }
}
