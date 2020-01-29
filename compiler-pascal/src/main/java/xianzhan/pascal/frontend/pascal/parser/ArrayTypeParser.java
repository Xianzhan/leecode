package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeFactory;
import xianzhan.pascal.intermediate.TypeForm;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Parse a Pascal array type specification.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
class ArrayTypeParser extends TypeSpecificationParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public ArrayTypeParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set for the [ token.
     */
    private static final EnumSet<PascalTokenType> LEFT_BRACKET_SET =
            SimpleTypeParser.SIMPLE_TYPE_START_SET.clone();

    static {
        LEFT_BRACKET_SET.add(PascalTokenType.LEFT_BRACKET);
        LEFT_BRACKET_SET.add(PascalTokenType.RIGHT_BRACKET);
    }

    /**
     * Synchronization set for the ] token.
     */
    private static final EnumSet<PascalTokenType> RIGHT_BRACKET_SET =
            EnumSet.of(
                    PascalTokenType.RIGHT_BRACKET,
                    PascalTokenType.OF,
                    PascalTokenType.SEMICOLON
            );

    /**
     * Synchronization set for OF.
     */
    private static final EnumSet<PascalTokenType> OF_SET =
            TypeSpecificationParser.TYPE_START_SET.clone();

    static {
        OF_SET.add(PascalTokenType.OF);
        OF_SET.add(PascalTokenType.SEMICOLON);
    }

    /**
     * Parse a Pascal array type specification.
     *
     * @param token the current token.
     * @return the array type specification.
     * @throws Exception if an error occurred.
     */
    @Override
    public TypeSpec parse(Token token) throws Exception {
        TypeSpec arrayType = TypeFactory.createType(TypeFormEnumImpl.ARRAY);
        // consume ARRAY
        token = nextToken();

        // Synchronize at the [ token.
        token = synchronize(LEFT_BRACKET_SET);
        if (token.getType() != PascalTokenType.LEFT_BRACKET) {
            errorHandler.flag(token, PascalErrorCode.MISSING_LEFT_BRACKET, this);
        }

        // Parse the list of index types.
        TypeSpec elementType = parseIndexTypeList(token, arrayType);

        // Synchronize at the ] token.
        token = synchronize(RIGHT_BRACKET_SET);
        if (token.getType() == PascalTokenType.RIGHT_BRACKET) {
            // consume [
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_RIGHT_BRACKET, this);
        }

        // Synchronize at OF.
        token = synchronize(OF_SET);
        if (token.getType() == PascalTokenType.OF) {
            // consume OF
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_OF, this);
        }

        // Parse the element type.
        elementType.setAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_TYPE, parseElementType(token));

        return arrayType;
    }

    /**
     * Synchronization set to start an index type.
     */
    private static final EnumSet<PascalTokenType> INDEX_START_SET =
            SimpleTypeParser.SIMPLE_TYPE_START_SET.clone();

    static {
        INDEX_START_SET.add(PascalTokenType.COMMA);
    }

    /**
     * Synchronization set to end an index type.
     */
    private static final EnumSet<PascalTokenType> INDEX_END_SET =
            EnumSet.of(
                    PascalTokenType.RIGHT_BRACKET,
                    PascalTokenType.OF,
                    PascalTokenType.SEMICOLON
            );

    /**
     * Synchronization set to follow an index type.
     */
    private static final EnumSet<PascalTokenType> INDEX_FOLLOW_SET =
            INDEX_START_SET.clone();

    static {
        INDEX_FOLLOW_SET.addAll(INDEX_END_SET);
    }

    /**
     * Parse the list of index type specifications.
     *
     * @param token     the current token.
     * @param arrayType the current array type specification.
     * @return the element type specification.
     * @throws Exception if an error occurred.
     */
    private TypeSpec parseIndexTypeList(Token token, TypeSpec arrayType) throws Exception {
        TypeSpec elementType = arrayType;
        boolean anotherIndex = false;

        // consume the [ token
        token = nextToken();

        // Parse the list of index type specifications.
        do {
            anotherIndex = false;

            // Parse the index type.
            token = synchronize(INDEX_START_SET);
            parseIndexType(token, elementType);

            // Synchronize at the , token
            token = synchronize(INDEX_FOLLOW_SET);
            TokenType tokenType = token.getType();
            if ((tokenType != PascalTokenType.COMMA && tokenType != PascalTokenType.RIGHT_BRACKET)) {
                if (INDEX_START_SET.contains(tokenType)) {
                    errorHandler.flag(token, PascalErrorCode.MISSING_COMMA, this);
                    anotherIndex = true;
                }
            } else if (tokenType == PascalTokenType.COMMA) {
                // Create an ARRAY element type object
                // for each subsequent index type.
                TypeSpec newElementType = TypeFactory.createType(TypeFormEnumImpl.ARRAY);
                elementType.setAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_TYPE, newElementType);
                elementType = newElementType;

                // consume the , token
                token = nextToken();
                anotherIndex = true;
            }
        } while (anotherIndex);

        return elementType;
    }

    /**
     * Parse an index type specification.
     *
     * @param token     the current token.
     * @param arrayType the current array type specification.
     * @throws Exception if an error occurred.
     */
    private void parseIndexType(Token token, TypeSpec arrayType) throws Exception {
        SimpleTypeParser simpleTypeParser = new SimpleTypeParser(this);
        TypeSpec indexType = simpleTypeParser.parse(token);
        arrayType.setAttribute(TypeKeyEnumImpl.ARRAY_INDEX_TYPE, indexType);

        if (indexType == null) {
            return;
        }

        TypeForm form = indexType.getForm();
        int count = 0;

        // Check the index type and set the element count.
        if (form == TypeFormEnumImpl.SUBRANGE) {
            Integer minValue =
                    (Integer) indexType.getAttribute(TypeKeyEnumImpl.SUBRANGE_MIN_VALUE);
            Integer maxValue =
                    (Integer) indexType.getAttribute(TypeKeyEnumImpl.SUBRANGE_MAX_VALUE);

            if ((minValue != null) && (maxValue != null)) {
                count = maxValue - minValue + 1;
            }
        } else if (form == TypeFormEnumImpl.ENUMERATION) {
            ArrayList<SymTabEntry> constants = (ArrayList<SymTabEntry>)
                    indexType.getAttribute(TypeKeyEnumImpl.ENUMERATION_CONSTANTS);
            count = constants.size();
        } else {
            errorHandler.flag(token, PascalErrorCode.INVALID_INDEX_TYPE, this);
        }
        arrayType.setAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_COUNT, count);
    }

    /**
     * Parse the element type specification.
     *
     * @param token the current token.
     * @return the element type specification.
     * @throws Exception if an error occurred.
     */
    private TypeSpec parseElementType(Token token) throws Exception {
        TypeSpecificationParser typeSpecificationParser = new TypeSpecificationParser(this);
        return typeSpecificationParser.parse(token);
    }
}
