package com.github.xianzhan.core.util;

import org.junit.Test;

/**
 * 描述：网络工具测试类
 *
 * @author Lee
 * @see NetUtils
 * @since 2017/8/28
 */
public class NetUtilsTest {

    @Test
    public void getLocalIp() {
        System.out.println(NetUtils.getLocalIp());
    }
}
