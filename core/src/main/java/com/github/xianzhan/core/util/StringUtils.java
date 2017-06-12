package com.github.xianzhan.core.util;

import java.util.regex.Pattern;

/**
 * 描述：字符串工具类
 *
 * @author Lee
 * @since 2017/6/10
 */
public class StringUtils {

    private static final String IPv4 = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";

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
        return value == null || "".equals(value);
    }
}
