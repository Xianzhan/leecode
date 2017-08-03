package com.github.xianzhan.http1_1.core;

/**
 * 描述：协议版本
 *
 * @author Lee
 * @since 2017/8/3
 */
public class ProtocolVersion {

    private final String protocol = "HTTP";
    private final double version = 1.1;

    public String getProtocol() {
        return protocol;
    }

    public double getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return getProtocol() + "/" + getVersion();
    }
}
