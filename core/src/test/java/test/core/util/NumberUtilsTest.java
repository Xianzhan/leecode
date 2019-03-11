package test.core.util;

import xianzhan.core.util.NumberUtils;

/**
 * 描述：
 *
 * @author Lee
 * @since 2018/11/7
 */
public class NumberUtilsTest {

    public static void main(String[] args) {
        testIsPowerOfTwo();
        testIsOdd();
    }

    private static void testIsPowerOfTwo() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i + "\t is power of two: " + NumberUtils.isPowerOfTwo(i));
        }
    }

    private static void testIsOdd() {
        for (int i = -10; i < 10; i++) {
            System.out.println(i + "\t is odd: " + NumberUtils.isOdd(i));
        }
    }
}
