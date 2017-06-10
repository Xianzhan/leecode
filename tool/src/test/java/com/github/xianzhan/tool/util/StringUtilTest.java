package com.github.xianzhan.tool.util;

import org.junit.Test;

/**
 * 描述：字符串工具测试类
 *
 * @author Lee
 * @since 2017/6/10
 */
public class StringUtilTest {

    @Test
    public void isIP() {
        String[] ips = {"1.1.1.1", "1", "2.2", "3.3.3", "4.4.4.4", "4.",
                "0.2.2.2", "0.0.0.0", "255.255.255.255", "256.256.256.256"};
        for (String ip : ips) {
            System.out.println(StringUtil.isIP(ip));
        }
    }
}
