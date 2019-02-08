package xianzhan.core.util;

/**
 * 字符串工具类
 *
 * @author xianzhan
 * @since 2018-11-11
 */
public class Strings {

    private static final String EMPTY = "";

    /**
     * 判断该字符串是否能被 Double 所解析
     *
     * @param d double 字符串
     * @return true 能被 Double 解析
     * @see Double#parseDouble(String)
     */
    public static boolean isDouble(String d) {
        final char[] chars = d.toCharArray();
        final int len = chars.length;

        // 小数点计数
        int pointCount = 0;
        for (int i = 0; i < len; i++) {
            final char c = chars[i];
            if (charInRange('0', c, '9')) {
                continue;
            }
            if (c == '+' || c == '-') {
                // 正负符号只能在第一位
                if (i != 0) {
                    return false;
                }
            } else if (c == '.') {
                if (pointCount > 1) {
                    return false;
                }
                pointCount++;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为 null 或者为空
     *
     * @param s 字符串
     * @return true 为 null 或者为 ""
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * 将字节数组转为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return EMPTY;
        }

        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            String s = Integer.toHexString(b);
            if (s.length() == 1) {
                builder.append('0');
            }
            builder.append(s);
        }
        return builder.toString();
    }

    private static boolean charInRange(char left, char target, char right) {
        return left <= target && target <= right;
    }
}
