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
