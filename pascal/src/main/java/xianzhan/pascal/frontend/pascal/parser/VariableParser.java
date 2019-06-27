package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeForm;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.TypeChecker;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;

import java.util.EnumSet;

/**
 * Parse a Pascal variable.
 *
 * @author xianzhan
 * @since 2019-06-25
 */
public class VariableParser extends StatementParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public VariableParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set to start a subscript or a field.
     */
    private static final EnumSet<PascalTokenType> SUBSCRIPT_FIELD_START_SET =
            EnumSet.of(
                    PascalTokenType.LEFT_BRACKET,
                    PascalTokenType.DOT
            );

    /**
     * Parse a variable.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        // Look up the identifier in the symbol table stack.
        String name = token.getText().toLowerCase();
        SymTabEntry variableId = symTabStack.lookup(name);

        // If not found, flag the error and enter the identifier
        // as an undefined identifier with an undefined type.
        if (variableId == null) {
            errorHandler.flag(token, PascalErrorCode.IDENTIFIER_UNDEFINED, this);
            variableId = symTabStack.enterLocal(name);
            variableId.setDefinition(DefinitionEnumImpl.UNDEFINED);
            variableId.setTypeSpec(Predefined.undefinedType);
        }

        return parse(token, variableId);
    }

    /**
     * Parse a variable.
     *
     * @param token      the initial token.
     * @param variableId the symbol table entry of the variable identifier.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    public ICodeNode parse(Token token, SymTabEntry variableId) throws Exception {
        // Check how the variable is defined.
        Definition definition = variableId.getDefinition();
        if ((definition != DefinitionEnumImpl.VARIABLE)
                && (definition != DefinitionEnumImpl.VALUE_PARM)
                && (definition != DefinitionEnumImpl.VAR_PARM)) {
            errorHandler.flag(token, PascalErrorCode.INVALID_IDENTIFIER_USAGE, this);
        }

        variableId.appendLineNumber(token.getLineNumber());

        ICodeNode variableNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.VARIABLE);
        variableNode.setAttribute(ICodeKeyEnumImpl.ID, variableId);

        // consume the identifier
        token = nextToken();

        // Parse array subscripts or record fields.
        TypeSpec variableType = variableId.getTypeSpec();
        while (SUBSCRIPT_FIELD_START_SET.contains(token.getType())) {
            ICodeNode subFldNode = token.getType() == PascalTokenType.LEFT_BRACKET
                    ? parseSubscripts(variableType)
                    : parseField(variableType);
            token = currentToken();

            // Update the variable's type.
            // The variable node adopts the SUBSCRIPTS or FIELD node.
            variableType = subFldNode.getTypeSpec();
            variableNode.addChild(subFldNode);
        }

        variableNode.setTypeSpec(variableType);
        return variableNode;
    }

    /**
     * Synchronization set for the ] token.
     */
    private static final EnumSet<PascalTokenType> RIGHT_BRACKET_SET =
            EnumSet.of(
                    PascalTokenType.RIGHT_BRACKET,
                    PascalTokenType.EQUALS,
                    PascalTokenType.SEMICOLON
            );

    /**
     * Parse a set of comma-separated subscript expressions.
     *
     * @param variableType the type of the array variable.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseSubscripts(TypeSpec variableType) throws Exception {
        Token token;
        ExpressionParser expressionParser = new ExpressionParser(this);

        // Create a SUBSCRIPTS node.
        ICodeNode subscriptsNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.SUBSCRIPTS);

        do {
            // consume the [ or , token
            token = nextToken();

            // The current variable is an array.
            if (variableType.getForm() == TypeFormEnumImpl.ARRAY) {

                // Parse the subscript expression.
                ICodeNode exprNode = expressionParser.parse(token);
                TypeSpec exprType = exprNode != null
                        ? exprNode.getTypeSpec()
                        : Predefined.undefinedType;

                // The subscript expression type must be assignment
                // compatible with the array index type.
                TypeSpec indexType = (TypeSpec) variableType.getAttribute(TypeKeyEnumImpl.ARRAY_INDEX_TYPE);
                if (!TypeChecker.areAssignmentCompatible(indexType, exprType)) {
                    errorHandler.flag(token, PascalErrorCode.INCOMPATIBLE_TYPES, this);
                }

                // The SUBSCRIPTS node adopts the subscript expression tree.
                subscriptsNode.addChild(exprNode);

                // Update the variable's type.
                variableType = (TypeSpec) variableType.getAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_TYPE);
            }

            // Not an array type, so too many subscripts.
            else {
                errorHandler.flag(token, PascalErrorCode.TOO_MANY_SUBSCRIPTS, this);
                expressionParser.parse(token);
            }

            token = currentToken();
        } while (token.getType() == PascalTokenType.COMMA);

        // Synchronize at the ] token.
        token = synchronize(RIGHT_BRACKET_SET);
        if (token.getType() == PascalTokenType.RIGHT_BRACKET) {
            // consume the ] token
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_RIGHT_BRACKET, this);
        }

        subscriptsNode.setTypeSpec(variableType);
        return subscriptsNode;
    }

    /**
     * Parse a record field.
     *
     * @param variableType the type of the record variable.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    private ICodeNode parseField(TypeSpec variableType)
            throws Exception {
        // Create a FIELD node.
        ICodeNode fieldNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.FIELD);

        // consume the . token
        Token token = nextToken();
        TokenType tokenType = token.getType();
        TypeForm variableForm = variableType.getForm();

        if ((tokenType == PascalTokenType.IDENTIFIER) && (variableForm == TypeFormEnumImpl.RECORD)) {
            SymTab symTab = (SymTab) variableType.getAttribute(TypeKeyEnumImpl.RECORD_SYMTAB);
            String fieldName = token.getText().toLowerCase();
            SymTabEntry fieldId = symTab.lookup(fieldName);

            if (fieldId != null) {
                variableType = fieldId.getTypeSpec();
                fieldId.appendLineNumber(token.getLineNumber());

                // Set the field identifier's name.
                fieldNode.setAttribute(ICodeKeyEnumImpl.ID, fieldId);
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_FIELD, this);
            }
        } else {
            errorHandler.flag(token, PascalErrorCode.INVALID_FIELD, this);
        }

        // consume the field identifier
        token = nextToken();

        fieldNode.setTypeSpec(variableType);
        return fieldNode;
    }
}
