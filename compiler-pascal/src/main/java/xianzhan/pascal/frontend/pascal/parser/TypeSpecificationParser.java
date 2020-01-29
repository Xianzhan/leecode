package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.TypeSpec;

import java.util.EnumSet;

/**
 * Parse a Pascal type specification.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
class TypeSpecificationParser extends PascalParserTD {

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public TypeSpecificationParser(PascalParserTD parent) {
        super(parent);
    }

    static final EnumSet<PascalTokenType> TYPE_START_SET =
            SimpleTypeParser.SIMPLE_TYPE_START_SET.clone();

    static {
        TYPE_START_SET.add(PascalTokenType.ARRAY);
        TYPE_START_SET.add(PascalTokenType.RECORD);
        TYPE_START_SET.add(PascalTokenType.SEMICOLON);
    }

    /**
     * Parse a Pascal type specification.
     *
     * @param token the current token.
     * @return the type specification.
     * @throws Exception if an error occurred.
     */
    public TypeSpec parse(Token token) throws Exception {
        // Synchronize at the start of a type specification.
        token = synchronize(TYPE_START_SET);

        switch ((PascalTokenType) token.getType()) {

            case ARRAY: {
                ArrayTypeParser arrayTypeParser = new ArrayTypeParser(this);
                return arrayTypeParser.parse(token);
            }

            case RECORD: {
                RecordTypeParser recordTypeParser = new RecordTypeParser(this);
                return recordTypeParser.parse(token);
            }

            default: {
                SimpleTypeParser simpleTypeParser = new SimpleTypeParser(this);
                return simpleTypeParser.parse(token);
            }
        }
    }
}
