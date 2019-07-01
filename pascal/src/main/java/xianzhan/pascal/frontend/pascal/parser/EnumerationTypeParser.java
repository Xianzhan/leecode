package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeFactory;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Parse a Pascal enumeration type specification.
 *
 * @author xianzhan
 * @since 2019-06-23
 */
public class EnumerationTypeParser extends TypeSpecificationParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public EnumerationTypeParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set to start an enumeration constant.
     */
    private static final EnumSet<PascalTokenType> ENUM_CONSTANT_START_SET =
            EnumSet.of(
                    PascalTokenType.IDENTIFIER,
                    PascalTokenType.COMMA
            );

    /**
     * Synchronization set to follow an enumeration definition.
     */
    private static final EnumSet<PascalTokenType> ENUM_DEFINITION_FOLLOW_SET =
            EnumSet.of(
                    PascalTokenType.RIGHT_PAREN,
                    PascalTokenType.SEMICOLON
            );

    static {
        ENUM_DEFINITION_FOLLOW_SET.addAll(DeclarationsParser.VAR_START_SET);
    }

    /**
     * Parse a Pascal enumeration type specification.
     *
     * @param token the current token.
     * @return the enumeration type specification.
     * @throws Exception if an error occurred.
     */
    @Override
    public TypeSpec parse(Token token) throws Exception {
        TypeSpec enumerationType = TypeFactory.createType(TypeFormEnumImpl.ENUMERATION);
        int value = -1;
        ArrayList<SymTabEntry> constants = new ArrayList<>();

        // consume the opening (
        token = nextToken();

        do {
            token = synchronize(ENUM_CONSTANT_START_SET);
            parseEnumerationIdentifier(token, ++value, enumerationType, constants);

            token = currentToken();
            TokenType tokenType = token.getType();

            // Look for the comma.
            if (tokenType == PascalTokenType.COMMA) {
                // consume the comma
                token = nextToken();

                if (ENUM_DEFINITION_FOLLOW_SET.contains(token.getType())) {
                    errorHandler.flag(token, PascalErrorCode.MISSING_IDENTIFIER, this);
                }
            } else if (ENUM_CONSTANT_START_SET.contains(tokenType)) {
                errorHandler.flag(token, PascalErrorCode.MISSING_COMMA, this);
            }
        } while (!ENUM_DEFINITION_FOLLOW_SET.contains(token.getType()));

        // Look for the closing ).
        if (token.getType() == PascalTokenType.RIGHT_PAREN) {
            // consume the )
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_RIGHT_PAREN, this);
        }

        enumerationType.setAttribute(TypeKeyEnumImpl.ENUMERATION_CONSTANTS, constants);
        return enumerationType;
    }

    /**
     * Parse an enumeration identifier.
     *
     * @param token           the current token.
     * @param value           the identifier's integer value (sequence number).
     * @param enumerationType the enumeration type specification.
     * @param constants       the array of symbol table entries for the
     *                        enumeration constants.
     * @throws Exception if an error occurred.
     */
    private void parseEnumerationIdentifier(Token token,
                                            int value,
                                            TypeSpec enumerationType,
                                            ArrayList<SymTabEntry> constants) throws Exception {
        TokenType tokenType = token.getType();

        if (tokenType == PascalTokenType.IDENTIFIER) {
            String name = token.getText().toLowerCase();
            SymTabEntry constantId = symTabStack.lookupLocal(name);

            if (constantId != null) {
                errorHandler.flag(token, PascalErrorCode.IDENTIFIER_REDEFINED, this);
            } else {
                constantId = symTabStack.enterLocal(name);
                constantId.setDefinition(DefinitionEnumImpl.ENUMERATION_CONSTANT);
                constantId.setTypeSpec(enumerationType);
                constantId.setAttribute(SymTabKeyImpl.CONSTANT_VALUE, value);
                constantId.appendLineNumber(token.getLineNumber());
                constants.add(constantId);
            }

            // consume the identifier
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_IDENTIFIER, this);
        }
    }
}
