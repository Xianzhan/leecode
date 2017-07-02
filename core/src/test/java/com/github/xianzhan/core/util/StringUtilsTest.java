package com.github.xianzhan.core.util;

import org.junit.Test;

/**
 * 描述：字符串工具测试类
 *
 * @author Lee
 * @since 2017/6/10
 */
public class StringUtilsTest {

    @Test
    public void isIP() {
        String[] ips = {"1.1.1.1", "1", "2.2", "3.3.3", "4.4.4.4", "4.",
                "0.2.2.2", "0.0.0.0", "255.255.255.255", "256.256.256.256"};
        for (String ip : ips) {
            System.out.println(StringUtils.isIP(ip));
        }
    }

    @Test
    public void deleteAny() {
        String str = "lskdjflsjaflskdjflskjdfoiwejfkldfjljk";
        System.out.println(StringUtils.deleteAny(str, "ls"));
    }

    @Test
    public void isRealNumber() {
        String[] strings = {"234", "45.", "234543.5656", ".156464"};
        for (String str : strings) {
            System.out.println(StringUtils.isRealNumber(str));
        }
    }

    @Test
    public void repeat() {
        String value = "abc";
        System.out.println(StringUtils.repeat(value, 3));
    }
}
