package xianzhan.pascal;

/**
 * Pascal Runtime Library:
 * Perform a runtime range check.
 *
 * @author xianzhan
 * @since 2019-07-21
 */
public class RangeChecker {
    public static void check(int value, int minValue, int maxValue) throws PascalRuntimeException {
        if ((value < minValue) || (value > maxValue)) {
            throw new PascalRuntimeException(String.format("Range error: %1d not in [%1d, %1d]", value, minValue, maxValue));
        }
    }
}
