package com.github.xianzhan.core.util;

import java.util.regex.Pattern;

/**
 * 描述：字符串工具类
 *
 * @author Lee
 * @since 2017/6/10
 */
public class StringUtils {

    /**
     * 空格
     */
    public static final String SPACE = " ";

    /**
     * TAB
     */
    public static final String TAB = "    ";

    /**
     * .
     */
    public static final String DOT = ".";

    /**
     * 斜杠
     */
    public static final String SLASH = "/";

    /**
     * 反斜杠
     */
    public static final String BACK_SLASH = "\\";

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    /**
     * 换行
     */
    public static final String LF = "\n";

    /**
     * 回车换行
     */
    public static final String CRLF = "\r\n";

    /**
     * 回程
     */
    public static final String CR = "\r";

    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 英文逗号
     */
    public static final String COMMA = ",";

    /**
     * {
     */
    public static final String DELIM_START = "{";

    /**
     * }
     */
    public static final String DELIM_END = "}";

    /**
     * [
     */
    public static final String BRACKET_START = "[";

    /**
     * ]
     */
    public static final String BRACKET_END = "]";

    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * space
     */
    public static final String HTML_NBSP  = "&nbsp;";
    /**
     * and &
     */
    public static final String HTML_AMP   = "&amp;";
    /**
     * quote "
     */
    public static final String HTML_QUOTE = "&quot;";
    /**
     * less than sign <
     */
    public static final String HTML_LT    = "&lt;";
    /**
     * greater than sign >
     */
    public static final String HTML_GT    = "&gt;";

    public static final String IPv4 = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}" +
            "(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";

    public static String repeat(String value, int time) {
        if (isEmpty(value))
            throw new NullPointerException("value 不能为空!");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < time; i++) {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 判断是否是实数(整数和小数的集合)
     *
     * @param value
     * @return
     */
    public static boolean isRealNumber(String value) {
        String regex = "^\\d+(\\.\\d+)?$";
        return isEmpty(value) ? false
                : Pattern.compile(regex).matcher(value).find();
    }

    /**
     * 删除指定的字符集合
     *
     * @param inString
     * @param charsToDelete
     * @return
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (isEmpty(inString) || isEmpty(charsToDelete)) {
            return inString;
        }

        StringBuilder sb = new StringBuilder(inString.length());
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            int index = charsToDelete.indexOf(c);
            if (index == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 检查字符串是否为 IP 地址
     *
     * @param value
     * @return
     */
    public static boolean isIP(String value) {
        return isEmpty(value) ? false
                : Pattern.compile(IPv4).matcher(value).find();
    }

    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     *
     * @param value 要检查的字符串
     * @return
     */
    public static boolean isEmpty(String value) {
        return value == null || EMPTY.equals(value);
    }

    private StringUtils() {
    }
}
