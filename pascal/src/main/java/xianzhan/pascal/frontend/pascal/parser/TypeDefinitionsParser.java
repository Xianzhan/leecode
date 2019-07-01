package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;

import java.util.EnumSet;

/**
 * Parse Pascal type definitions.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public class TypeDefinitionsParser extends DeclarationsParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public TypeDefinitionsParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set for a type identifier.
     */
    private static final EnumSet<PascalTokenType> IDENTIFIER_SET =
            DeclarationsParser.VAR_START_SET.clone();

    static {
        IDENTIFIER_SET.add(PascalTokenType.IDENTIFIER);
    }

    /**
     * Synchronization set for the = token.
     */
    private static final EnumSet<PascalTokenType> EQUALS_SET =
            ConstantDefinitionsParser.CONSTANT_START_SET.clone();

    static {
        EQUALS_SET.add(PascalTokenType.EQUALS);
        EQUALS_SET.add(PascalTokenType.SEMICOLON);
    }

    /**
     * Synchronization set for what follows a definition or declaration.
     */
    private static final EnumSet<PascalTokenType> FOLLOW_SET =
            EnumSet.of(PascalTokenType.SEMICOLON);

    /**
     * Synchronization set for the start of the next definition or declaration.
     */
    private static final EnumSet<PascalTokenType> NEXT_START_SET =
            DeclarationsParser.VAR_START_SET.clone();

    static {
        NEXT_START_SET.add(PascalTokenType.SEMICOLON);
        NEXT_START_SET.add(PascalTokenType.IDENTIFIER);
    }

    /**
     * Parse type definitions.
     *
     * @param token    the initial token.
     * @param parentId the symbol table entry of the parent routine's name.
     * @return null
     * @throws Exception if an error occurred.
     */
    @Override
    public SymTabEntry parse(Token token, SymTabEntry parentId) throws Exception {
        token = synchronize(IDENTIFIER_SET);

        // Loop to parse a sequence of type definitions
        // separated by semicolons.
        while (token.getType() == PascalTokenType.IDENTIFIER) {
            String name = token.getText().toLowerCase();
            SymTabEntry typeId = symTabStack.lookupLocal(name);

            // Enter the new identifier into the symbol table
            // but don't set how it's defined yet.
            if (typeId == null) {
                typeId = symTabStack.enterLocal(name);
                typeId.appendLineNumber(token.getLineNumber());
            } else {
                errorHandler.flag(token, PascalErrorCode.IDENTIFIER_REDEFINED, this);
                typeId = null;
            }

            token = nextToken();  // consume the identifier token

            // Synchronize on the = token.
            token = synchronize(EQUALS_SET);
            if (token.getType() == PascalTokenType.EQUALS) {
                token = nextToken();  // consume the =
            } else {
                errorHandler.flag(token, PascalErrorCode.MISSING_EQUALS, this);
            }

            // Parse the type specification.
            TypeSpecificationParser typeSpecificationParser = new TypeSpecificationParser(this);
            TypeSpec type = typeSpecificationParser.parse(token);

            // Set identifier to be a type and set its type specificationt.
            if (typeId != null) {
                typeId.setDefinition(DefinitionEnumImpl.TYPE);
            }

            // Cross-link the type identifier and the type specification.
            if ((type != null) && (typeId != null)) {
                if (type.getIdentifier() == null) {
                    type.setIdentifier(typeId);
                }
                typeId.setTypeSpec(type);
            } else {
                token = synchronize(FOLLOW_SET);
            }

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

        return null;
    }
}
