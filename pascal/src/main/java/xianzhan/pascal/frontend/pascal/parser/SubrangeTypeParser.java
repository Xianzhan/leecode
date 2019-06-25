package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.TypeFactory;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;

/**
 * Parse a Pascal subrange type specification.
 *
 * @author xianzhan
 * @since 2019-06-23
 */
public class SubrangeTypeParser extends TypeSpecificationParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public SubrangeTypeParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Parse a Pascal subrange type specification.
     *
     * @param token the current token.
     * @return the subrange type specification.
     * @throws Exception if an error occurred.
     */
    @Override
    public TypeSpec parse(Token token) throws Exception {
        TypeSpec subrangeType = TypeFactory.createType(TypeFormEnumImpl.SUBRANGE);
        Object minValue = null;
        Object maxValue = null;

        // Parse the minimum constant.
        Token constantToken = token;
        ConstantDefinitionsParser constantParser = new ConstantDefinitionsParser(this);
        minValue = constantParser.parseConstant(token);

        // Set the minimum constant's type.
        TypeSpec minType = constantToken.getType() == PascalTokenType.IDENTIFIER
                ? constantParser.getConstantType(constantToken)
                : constantParser.getConstantType(minValue);

        minValue = checkValueType(constantToken, minValue, minType);

        token = currentToken();
        boolean sawDotDot = false;

        // Look for the .. token.
        if (token.getType() == PascalTokenType.DOT_DOT) {
            // consume the .. token
            token = nextToken();
            sawDotDot = true;
        }

        TokenType tokenType = token.getType();

        // At the start of the maximum constant?
        if (ConstantDefinitionsParser.CONSTANT_START_SET.contains(tokenType)) {
            if (!sawDotDot) {
                errorHandler.flag(token, PascalErrorCode.MISSING_DOT_DOT, this);
            }

            // Parse the maximum constant.
            token = synchronize(ConstantDefinitionsParser.CONSTANT_START_SET);
            constantToken = token;
            maxValue = constantParser.parseConstant(token);

            // Set the maximum constant's type.
            TypeSpec maxType = constantToken.getType() == PascalTokenType.IDENTIFIER
                    ? constantParser.getConstantType(constantToken)
                    : constantParser.getConstantType(maxValue);

            maxValue = checkValueType(constantToken, maxValue, maxType);

            // Are the min and max value types valid?
            if ((minType == null) || (maxType == null)) {
                errorHandler.flag(constantToken, PascalErrorCode.INCOMPATIBLE_TYPES, this);
            }

            // Are the min and max value types the same?
            else if (minType != maxType) {
                errorHandler.flag(constantToken, PascalErrorCode.INVALID_SUBRANGE_TYPE, this);
            }

            // Min value > max value?
            else if ((minValue != null) && (maxValue != null) &&
                    ((Integer) minValue >= (Integer) maxValue)) {
                errorHandler.flag(constantToken, PascalErrorCode.MIN_GT_MAX, this);
            }
        } else {
            errorHandler.flag(constantToken, PascalErrorCode.INVALID_SUBRANGE_TYPE, this);
        }

        subrangeType.setAttribute(TypeKeyEnumImpl.SUBRANGE_BASE_TYPE, minType);
        subrangeType.setAttribute(TypeKeyEnumImpl.SUBRANGE_MIN_VALUE, minValue);
        subrangeType.setAttribute(TypeKeyEnumImpl.SUBRANGE_MAX_VALUE, maxValue);

        return subrangeType;
    }

    /**
     * Check a value of a type specification.
     *
     * @param token the current token.
     * @param value the value.
     * @param type  the type specifiction.
     * @return the value.
     */
    private Object checkValueType(Token token, Object value, TypeSpec type) {
        if (type == null) {
            return value;
        }
        if (type == Predefined.integerType) {
            return value;
        } else if (type == Predefined.charType) {
            char ch = ((String) value).charAt(0);
            return Character.getNumericValue(ch);
        } else if (type.getForm() == TypeFormEnumImpl.ENUMERATION) {
            return value;
        } else {
            errorHandler.flag(token, PascalErrorCode.INVALID_SUBRANGE_TYPE, this);
            return value;
        }
    }
}
