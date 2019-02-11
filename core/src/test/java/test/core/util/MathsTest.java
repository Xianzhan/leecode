package test.core.util;

import xianzhan.core.util.NumberUtils;

/**
 * 描述：
 *
 * @author Lee
 * @since 2018/11/7
 */
public class MathsTest {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(i + "\t is power of two: " + NumberUtils.isPowerOfTwo(i));
        }
    }

}
