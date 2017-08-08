package com.github.xianzhan.http1_1.core;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/8/7
 */
public enum HTTPStatusCode {

    /**
     * 	初始的请求已经接受,客户应当继续发送请求的其余部分
     */
    Continue(100, "Continue"),

    /**
     * 服务器将遵从客户的请求转换到另外一种协议
     */
    SwitchingProtocols(101, "Switching Protocols"),

    /**
     * 一切正常,对GET和POST请求的应答文档跟在后面
     */
    OK(200, "OK"),

    /**
     * 无法找到指定位置的资源.这也是一个常用的应答
     */
    NotFound(404, "Not Found");



    private int statusCode;
    private String description;

    HTTPStatusCode(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }
}
