package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Parse Pascal variable declarations.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public class VariableDeclarationsParser extends DeclarationsParser {

    /**
     * How to define the identifier
     */
    private Definition definition;

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public VariableDeclarationsParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Setter.
     *
     * @param definition the definition to set.
     */
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    /**
     * Synchronization set for a variable identifier.
     */
    static final EnumSet<PascalTokenType> IDENTIFIER_SET =
            DeclarationsParser.VAR_START_SET.clone();

    static {
        IDENTIFIER_SET.add(PascalTokenType.IDENTIFIER);
        IDENTIFIER_SET.add(PascalTokenType.END);
        IDENTIFIER_SET.add(PascalTokenType.SEMICOLON);
    }

    /**
     * Synchronization set for the start of the next definition or declaration.
     */
    static final EnumSet<PascalTokenType> NEXT_START_SET =
            DeclarationsParser.ROUTINE_START_SET.clone();

    static {
        NEXT_START_SET.add(PascalTokenType.IDENTIFIER);
        NEXT_START_SET.add(PascalTokenType.SEMICOLON);
    }

    /**
     * Parse variable declarations.
     *
     * @param token the initial token.
     * @throws Exception if an error occurred.
     */
    @Override
    public void parse(Token token) throws Exception {
        token = synchronize(IDENTIFIER_SET);

        // Loop to parse a sequence of variable declarations
        // separated by semicolons.
        while (token.getType() == PascalTokenType.IDENTIFIER) {

            // Parse the identifier sublist and its type specification.
            parseIdentifierSublist(token);

            token = currentToken();
            TokenType tokenType = token.getType();

            // Look for one or more semicolons after a definition.
            if (tokenType == PascalTokenType.SEMICOLON) {
                while (token.getType() == PascalTokenType.SEMICOLON) {
                    // consume the ;
                    token = nextToken();
                }
            }

            // If at the start of the next definition or declaration,
            // then missing a semicolon.
            else if (NEXT_START_SET.contains(tokenType)) {
                errorHandler.flag(token, PascalErrorCode.MISSING_SEMICOLON, this);
            }

            token = synchronize(IDENTIFIER_SET);
        }
    }

    /**
     * Synchronization set to start a sublist identifier.
     */
    static final EnumSet<PascalTokenType> IDENTIFIER_START_SET =
            EnumSet.of(
                    PascalTokenType.IDENTIFIER,
                    PascalTokenType.COMMA
            );

    /**
     * Synchronization set to follow a sublist identifier.
     */
    private static final EnumSet<PascalTokenType> IDENTIFIER_FOLLOW_SET =
            EnumSet.of(
                    PascalTokenType.COLON,
                    PascalTokenType.SEMICOLON
            );

    static {
        IDENTIFIER_FOLLOW_SET.addAll(DeclarationsParser.VAR_START_SET);
    }

    /**
     * Synchronization set for the , token.
     */
    private static final EnumSet<PascalTokenType> COMMA_SET =
            EnumSet.of(
                    PascalTokenType.COMMA,
                    PascalTokenType.COLON,
                    PascalTokenType.IDENTIFIER,
                    PascalTokenType.SEMICOLON
            );

    /**
     * Parse a sublist of identifiers and their type specification.
     *
     * @param token the current token.
     * @return the sublist of identifiers in a declaration.
     * @throws Exception if an error occurred.
     */
    protected ArrayList<SymTabEntry> parseIdentifierSublist(Token token) throws Exception {
        ArrayList<SymTabEntry> sublist = new ArrayList<>();

        do {
            token = synchronize(IDENTIFIER_START_SET);
            SymTabEntry id = parseIdentifier(token);

            if (id != null) {
                sublist.add(id);
            }

            token = synchronize(COMMA_SET);
            TokenType tokenType = token.getType();

            // Look for the comma.
            if (tokenType == PascalTokenType.COMMA) {
                token = nextToken();  // consume the comma

                if (IDENTIFIER_FOLLOW_SET.contains(token.getType())) {
                    errorHandler.flag(token, PascalErrorCode.MISSING_IDENTIFIER, this);
                }
            } else if (IDENTIFIER_START_SET.contains(tokenType)) {
                errorHandler.flag(token, PascalErrorCode.MISSING_COMMA, this);
            }
        } while (!IDENTIFIER_FOLLOW_SET.contains(token.getType()));

        // Parse the type specification.
        TypeSpec type = parseTypeSpec(token);

        // Assign the type specification to each identifier in the list.
        for (SymTabEntry variableId : sublist) {
            variableId.setTypeSpec(type);
        }

        return sublist;
    }

    /**
     * Parse an identifier.
     *
     * @param token the current token.
     * @return the symbol table entry of the identifier.
     * @throws Exception if an error occurred.
     */
    private SymTabEntry parseIdentifier(Token token) throws Exception {
        SymTabEntry id = null;

        if (token.getType() == PascalTokenType.IDENTIFIER) {
            String name = token.getText().toLowerCase();
            id = symTabStack.lookupLocal(name);

            // Enter a new identifier into the symbol table.
            if (id == null) {
                id = symTabStack.enterLocal(name);
                id.setDefinition(definition);
                id.appendLineNumber(token.getLineNumber());
            } else {
                errorHandler.flag(token, PascalErrorCode.IDENTIFIER_REDEFINED, this);
            }

            // consume the identifier token
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_IDENTIFIER, this);
        }

        return id;
    }

    /**
     * Synchronization set for the : token.
     */
    private static final EnumSet<PascalTokenType> COLON_SET =
            EnumSet.of(
                    PascalTokenType.COLON,
                    PascalTokenType.SEMICOLON
            );

    /**
     * Parse the type specification.
     *
     * @param token the current token.
     * @return the type specification.
     * @throws Exception if an error occurs.
     */
    protected TypeSpec parseTypeSpec(Token token) throws Exception {
        // Synchronize on the : token.
        token = synchronize(COLON_SET);
        if (token.getType() == PascalTokenType.COLON) {
            // consume the :
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_COLON, this);
        }

        // Parse the type specification.
        TypeSpecificationParser typeSpecificationParser = new TypeSpecificationParser(this);
        TypeSpec type = typeSpecificationParser.parse(token);

        return type;
    }
}
