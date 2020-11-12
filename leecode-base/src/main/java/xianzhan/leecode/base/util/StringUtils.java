package xianzhan.leecode.base.util;

/**
 * 字符串工具类, 用于处理和字符串相关的方法集合
 * <p>
 * 任何返回字符串的方法, 如果参数为 null 的, 都将返回空字符串
 *
 * @author xianzhan
 * @since 2020-11-01
 */
public class StringUtils {

    public static final String EMPTY = "";

    /**
     * 判断字符串是否为 null 或者为空字符串
     *
     * @param s 字符串
     * @return 字符串是否为 null 或者为空字符串
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * 返回字符串的长度
     *
     * @param s 字符串
     * @return 字符串的长度
     */
    public static int length(String s) {
        return s == null ? 0 : s.length();
    }
}
