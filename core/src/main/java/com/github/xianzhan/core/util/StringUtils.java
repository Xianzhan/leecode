package com.github.xianzhan.core.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
    public static final String HTML_NBSP = "&nbsp;";
    /**
     * and &
     */
    public static final String HTML_AMP = "&amp;";
    /**
     * quote "
     */
    public static final String HTML_QUOTE = "&quot;";
    /**
     * less than sign <
     */
    public static final String HTML_LT = "&lt;";
    /**
     * greater than sign >
     */
    public static final String HTML_GT = "&gt;";

    public static final String IPv4 = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}" +
            "(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";

    /**
     * 分片
     *
     * @param value
     * @param start
     * @param end
     * @param step
     * @return
     */
    public static String slice(String value, int start, int end, int step) {
        ifValueIsEmptyThrow(value);

        int startAbs = Math.abs(start);
        int endAbs = Math.abs(end);
        int stepAbs = Math.abs(step);
        ifLengthTooSmallThrow(value.length(), startAbs, endAbs, stepAbs);

        int length = value.length();
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i += step) {
            sb.append(value.charAt(i));
        }
        return sb.toString();
    }

    /**
     * 重复字符串
     *
     * @param value 字符串
     * @param time  次数
     * @return value * time
     */
    public static String repeat(String value, int time) {
        ifValueIsEmptyThrow(value);
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
        return !isEmpty(value) && value.matches(regex);
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
        return !isEmpty(value) && value.matches(IPv4);
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

    public static InputStream toInputStream(String content) {
        return isEmpty(content) ? null : new ByteArrayInputStream(content.getBytes());
    }

    private StringUtils() {
    }

    /**
     * 检查字符串值是否为空
     *
     * @param value 字符串
     */
    private static void ifValueIsEmptyThrow(String value) {
        if (isEmpty(value)) {
            throw new NullPointerException("value 不能为空!");
        }
    }

    /**
     * 字符串长度与其它值比较，其它值是否太大
     *
     * @param length
     * @param compares
     */
    private static void ifLengthTooSmallThrow(int length, int... compares) {
        for (int compare : compares) {
            if (length < compare) {
                throw new IllegalArgumentException("比较的值比字符串长度大");
            }
        }
    }
}
