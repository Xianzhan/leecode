package xianzhan.pascal.frontend.pascal.tokens;

import xianzhan.pascal.frontend.Source;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalToken;
import xianzhan.pascal.frontend.pascal.PascalTokenType;

/**
 * An unsigned integer is a sequence of digits. A Pascal integer number token
 * is an unsigned integer. A Pascal real number token begins with an unsigned
 * integer (the whole part), which is followed by either.
 * <p>
 * - A decimal point followed by an unsigned integer (the fraction part), or
 * <br>
 * - An E or e, optionally followed by + or -, followed by an unsigned
 * integer (the exponent part), or
 * <br>
 * - A fraction part followed by an exponent part.
 *
 * @author Ronald Mak
 */
public class PascalNumberToken extends PascalToken {
    private static final int MAX_EXPONENT = 37;

    /**
     * Constructor.
     *
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public PascalNumberToken(Source source) throws Exception {
        super(source);
    }

    /**
     * Extract a Pascal number token from the source.
     *
     * @throws Exception if an error occurred.
     */
    @Override
    protected void extract() throws Exception {
        // token's characters
        StringBuilder textBuffer = new StringBuilder();
        extractNumber(textBuffer);
        text = textBuffer.toString();
    }

    /**
     * Extract a Pascal number token from the source.
     *
     * @param textBuffer the buffer to append the token's characters.
     * @throws Exception if an error occurred.
     */
    protected void extractNumber(StringBuilder textBuffer) throws Exception {
        // digits before the decimal point
        String wholeDigits;
        // digits after the decimal point
        String fractionDigits = null;
        // exponent digits
        String exponentDigits = null;
        // exponent sign '+' or '-'
        char exponentSign = '+';
        // true if saw .. token
        boolean sawDotDot = false;
        // current character
        char currentChar;

        // assume INTEGER token type for now
        type = PascalTokenType.INTEGER;

        // Extract the digits of the whole part of the number.
        wholeDigits = unsignedIntegerDigits(textBuffer);
        if (type == PascalTokenType.ERROR) {
            return;
        }

        // Is there a . ?
        // It could be a decimal point or the start of a .. token.
        currentChar = currentChar();
        if (currentChar == '.') {
            if (peekChar() == '.') {
                // it's a ".." token, so don't consume it
                sawDotDot = true;
            } else {
                // decimal point, so token type is REAL
                type = PascalTokenType.REAL;
                textBuffer.append(currentChar);
                // consume decimal point
                currentChar = nextChar();

                // Collect the digits of the fraction part of the number.
                fractionDigits = unsignedIntegerDigits(textBuffer);
                if (type == PascalTokenType.ERROR) {
                    return;
                }
            }
        }

        // Is there an exponent part?
        // There cannot be an exponent if we already saw a ".." token.
        currentChar = currentChar();
        if (!sawDotDot && ((currentChar == 'E') || (currentChar == 'e'))) {
            // exponent, so token type is REAL
            type = PascalTokenType.REAL;
            textBuffer.append(currentChar);
            // consume 'E' or 'e'
            currentChar = nextChar();

            // Exponent sign?
            if ((currentChar == '+') || (currentChar == '-')) {
                textBuffer.append(currentChar);
                exponentSign = currentChar;
                // consume '+' or '-'
                currentChar = nextChar();
            }

            // Extract the digits of the exponent.
            exponentDigits = unsignedIntegerDigits(textBuffer);
        }

        // Compute the value of an integer number token.
        if (type == PascalTokenType.INTEGER) {
            int integerValue = computeIntegerValue(wholeDigits);

            if (type != PascalTokenType.ERROR) {
                value = integerValue;
            }
        }

        // Compute the value of a real number token.
        else if (type == PascalTokenType.REAL) {
            float floatValue = computeFloatValue(wholeDigits, fractionDigits, exponentDigits, exponentSign);

            if (type != PascalTokenType.ERROR) {
                value = floatValue;
            }
        }
    }

    /**
     * Extract and return the digits of an unsigned integer.
     *
     * @param textBuffer the buffer to append the token's characters.
     * @return the string of digits.
     * @throws Exception if an error occurred.
     */
    private String unsignedIntegerDigits(StringBuilder textBuffer)
            throws Exception {
        char currentChar = currentChar();

        // Must have at least one digit.
        if (!Character.isDigit(currentChar)) {
            type = PascalTokenType.ERROR;
            value = PascalErrorCode.INVALID_NUMBER;
            return null;
        }

        // Extract the digits.
        StringBuilder digits = new StringBuilder();
        while (Character.isDigit(currentChar)) {
            textBuffer.append(currentChar);
            digits.append(currentChar);
            // consume digit
            currentChar = nextChar();
        }

        return digits.toString();
    }

    /**
     * Compute and return the integer value of a string of digits.
     * Check for overflow.
     *
     * @param digits the string of digits.
     * @return the integer value.
     */
    private int computeIntegerValue(String digits) {
        // Return 0 if no digits.
        if (digits == null) {
            return 0;
        }

        int integerValue = 0;
        // overflow occurred if prevValue > integerValue
        int prevValue = -1;
        int index = 0;

        // Loop over the digits to compute the integer value
        // as long as there is no overflow.
        while ((index < digits.length()) && (integerValue >= prevValue)) {
            prevValue = integerValue;
            integerValue = 10 * integerValue
                           + Character.getNumericValue(digits.charAt(index++));
        }

        // No overflow:  Return the integer value.
        if (integerValue >= prevValue) {
            return integerValue;
        }

        // Overflow:  Set the integer out of range error.
        else {
            type = PascalTokenType.ERROR;
            value = PascalErrorCode.RANGE_INTEGER;
            return 0;
        }
    }

    /**
     * Compute and return the float value of a real number.
     *
     * @param wholeDigits    the string of digits before the decimal point.
     * @param fractionDigits the string of digits after the decimal point.
     * @param exponentDigits the string of exponent digits.
     * @param exponentSign   the exponent sign.
     * @return the float value.
     */
    private float computeFloatValue(String wholeDigits, String fractionDigits,
                                    String exponentDigits, char exponentSign) {
        double floatValue = 0.0;
        int exponentValue = computeIntegerValue(exponentDigits);
        // whole and fraction digits
        String digits = wholeDigits;

        // Negate the exponent if the exponent sign is '-'.
        if (exponentSign == '-') {
            exponentValue = -exponentValue;
        }

        // If there are any fraction digits, adjust the exponent value
        // and append the fraction digits.
        if (fractionDigits != null) {
            exponentValue -= fractionDigits.length();
            digits += fractionDigits;
        }

        // Check for a real number out of range error.
        if (Math.abs(exponentValue + wholeDigits.length()) > MAX_EXPONENT) {
            type = PascalTokenType.ERROR;
            value = PascalErrorCode.RANGE_REAL;
            return 0.0f;
        }

        // Loop over the digits to compute the float value.
        int index = 0;
        while (index < digits.length()) {
            floatValue = 10 * floatValue +
                         Character.getNumericValue(digits.charAt(index++));
        }

        // Adjust the float value based on the exponent value.
        if (exponentValue != 0) {
            floatValue *= Math.pow(10, exponentValue);
        }

        return (float) floatValue;
    }
}
