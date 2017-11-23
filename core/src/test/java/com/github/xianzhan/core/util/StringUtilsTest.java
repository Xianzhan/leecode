package com.github.xianzhan.core.util;

public class StringUtilsTest {
    public static void main(String[] args) {
        System.out.println(StringUtils.slice("0123456789", -3, 10, 1));
        System.out.println(StringUtils.join(",", new String[]{"123", "345"}));
    }
}
