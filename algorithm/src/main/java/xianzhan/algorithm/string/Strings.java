package xianzhan.algorithm.string;

/**
 * 字符串
 *
 * @author xianzhan
 * @since 2018-09-14
 */
public class Strings {

    /**
     * 移动字符串，p 位置字符移动到第一位
     *
     * @param str 字符串
     * @param p   移动位置
     * @return 移动后的字符串
     */
    public String rotateString(String str, int p) {
        final int len = str.length();
        return (str + str).substring(p, len + p);
    }
}
