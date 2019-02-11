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
     * @param num 待判断的数
     * @return true 是 2 的幂
     */
    public static boolean isPowerOfTwo(int num) {
        return (num & -num) == num;
    }
}
