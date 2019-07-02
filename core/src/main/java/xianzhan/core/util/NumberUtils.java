package xianzhan.core.util;

/**
 * 描述：数学相关工具类
 *
 * @author Lee
 * @since 2018-11-7
 */
public class NumberUtils {

    /**
     * 判断一个数是否为 2 的幂
     *
     * @param num 待判断的数
     * @return true 是 2 的幂
     */
    public static boolean isPowerOfTwo(int num) {
        return (num & -num) == num;
    }

    /**
     * 判断一个数字是否为奇数
     *
     * @param num int
     * @return true 奇数
     */
    public static boolean isOdd(int num) {
        return (num & 1) == 1;
    }

    /**
     * 加法, 位运算实现
     *
     * @param a 被加数
     * @param b 加数
     * @return 和
     */
    public static int add(int a, int b) {
        int sum = a ^ b;
        int carry = (a & b) << 1;
        while (carry != 0) {
            int c = sum;
            int d = carry;
            sum = c ^ d;
            carry = (c & d) << 1;
        }
        return sum;
    }

    /**
     * 减法, 位运算实现
     *
     * @param a 被减数
     * @param b 减数
     * @return 差
     */
    public static int sub(int a, int b) {
        // 先求减数的补码
        int subtractor = add(~b, 1);
        return add(a, subtractor);
    }

    /**
     * 乘法, 位运算实现
     *
     * @param a 被乘数
     * @param b 乘数
     * @return 积
     */
    public static int mul(int a, int b) {
        // 将乘数和被乘数都取绝对值　
        int multiplicand = a < 0 ? add(~a, 1) : a;
        int multiplier = b < 0 ? add(~b, 1) : b;

        // 计算绝对值的乘积　　
        int product = 0;
        while (multiplier > 0) {
            if ((multiplier & 0x1) > 0) {
                // 每次考察乘数的最后一位
                product = add(product, multiplicand);
            }
            // 每运算一次，被乘数要左移一位
            multiplicand = multiplicand << 1;
            // 每运算一次，乘数要右移一位（可对照上图理解）
            multiplier = multiplier >> 1;
        }
        //计算乘积的符号　　
        if ((a ^ b) < 0) {
            product = add(~product, 1);
        }
        return product;
    }

    /**
     * 除法, 位运算实现
     *
     * @param a 被除数
     * @param b 除数
     * @return 商
     */
    public static int div(int a, int b) {
        // 先取被除数和除数的绝对值
        int dividend = a > 0 ? a : add(~a, 1);
        int divisor = b > 0 ? a : add(~b, 1);
        // 商
        int quotient = 0;
        // 余数
        int remainder = 0;
        for (int i = 31; i >= 0; i--) {
            // 比较 dividend 是否大于 divisor 的 (1<<i) 次方，
            // 不要将 dividend 与 (divisor<<i) 比较，
            // 而是用 (dividend>>i) 与 divisor 比较，
            // 效果一样，但是可以避免因 (divisor<<i) 操作可能导致的溢出，
            // 如果溢出则会可能 dividend 本身小于 divisor，
            // 但是溢出导致 dividend 大于 divisor
            if ((dividend >> i) >= divisor) {
                quotient = add(quotient, 1 << i);
                dividend = sub(dividend, divisor << i);
            }
        }
        // 确定商的符号
        if ((a ^ b) < 0) {
            // 如果除数和被除数异号，则商为负数
            quotient = add(~quotient, 1);
        }
        // 确定余数符号
        remainder = b > 0 ? dividend : add(~dividend, 1);
        // 返回商
        return quotient;
    }
}
