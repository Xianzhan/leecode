package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;

import java.util.EnumSet;

/**
 * Parse a simple Pascal type (identifier, subrange, enumeration) specification.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
class SimpleTypeParser extends TypeSpecificationParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public SimpleTypeParser(PascalParserTD parent) {
        super(parent);
    }

    static final EnumSet<PascalTokenType> SIMPLE_TYPE_START_SET =
            ConstantDefinitionsParser.CONSTANT_START_SET.clone();

    static {
        SIMPLE_TYPE_START_SET.add(PascalTokenType.LEFT_PAREN);
        SIMPLE_TYPE_START_SET.add(PascalTokenType.COMMA);
        SIMPLE_TYPE_START_SET.add(PascalTokenType.SEMICOLON);
    }

    /**
     * Parse a simple Pascal type specification.
     *
     * @param token the current token.
     * @return the simple type specification.
     * @throws Exception if an error occurred.
     */
    @Override
    public TypeSpec parse(Token token) throws Exception {
        // Synchronize at the start of a simple type specification.
        token = synchronize(SIMPLE_TYPE_START_SET);

        switch ((PascalTokenType) token.getType()) {

            case IDENTIFIER: {
                String name = token.getText().toLowerCase();
                SymTabEntry id = symTabStack.lookup(name);

                if (id != null) {
                    Definition definition = id.getDefinition();

                    // It's either a type identifier
                    // or the start of a subrange type.
                    if (definition == DefinitionEnumImpl.TYPE) {
                        id.appendLineNumber(token.getLineNumber());
                        // consume the identifier
                        token = nextToken();

                        // Return the type of the referent type.
                        return id.getTypeSpec();
                    } else if ((definition != DefinitionEnumImpl.CONSTANT) && (definition != DefinitionEnumImpl.ENUMERATION_CONSTANT)) {
                        errorHandler.flag(token, PascalErrorCode.NOT_TYPE_IDENTIFIER, this);
                        // consume the identifier
                        token = nextToken();
                        return null;
                    } else {
                        SubrangeTypeParser subrangeTypeParser = new SubrangeTypeParser(this);
                        return subrangeTypeParser.parse(token);
                    }
                } else {
                    errorHandler.flag(token, PascalErrorCode.IDENTIFIER_UNDEFINED, this);
                    // consume the identifier
                    token = nextToken();
                    return null;
                }
            }

            case LEFT_PAREN: {
                EnumerationTypeParser enumerationTypeParser = new EnumerationTypeParser(this);
                return enumerationTypeParser.parse(token);
            }

            case COMMA:
            case SEMICOLON: {
                errorHandler.flag(token, PascalErrorCode.INVALID_TYPE, this);
                return null;
            }

            default: {
                SubrangeTypeParser subrangeTypeParser = new SubrangeTypeParser(this);
                return subrangeTypeParser.parse(token);
            }
        }
    }
}
