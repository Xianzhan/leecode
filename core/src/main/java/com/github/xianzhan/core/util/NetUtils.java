package com.github.xianzhan.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 描述：网络工具类
 *
 * @author Lee
 * @since 2017/8/28
 */
public class NetUtils {

    /**
     * 获取本地 IP 地址
     * @return IP 地址字符串
     */
    public static String getLocalIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }
}
