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
        testAdd();
        testSub();
        testMul();
        testDiv();
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

    private static void testAdd() {
        int sum = 0;
        for (int i = 1; i < 100; i++) {
            sum = NumberUtils.add(sum, i);
        }
        System.out.println("1 + ... + 99 = " + sum);
    }

    private static void testSub() {
        int sum = 4950;
        for (int i = 1; i < 100; i++) {
            sum = NumberUtils.sub(sum, i);
        }
        System.out.println("4950 - ... - 1 = " + sum);
    }

    private static void testMul() {
        int mul = 1;
        for (int i = 1; i < 10; i++) {
            mul = NumberUtils.mul(mul, i);
        }
        System.out.println("1 * ... * 9 = " + mul);
    }

    private static void testDiv() {
        int div = 362880;
        for (int i = 1; i < 10; i++) {
            div = NumberUtils.div(div, i);
        }
        System.out.println("362880 / ... / 1 = " + div);
    }
}
