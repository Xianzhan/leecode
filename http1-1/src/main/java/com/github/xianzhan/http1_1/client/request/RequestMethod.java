package com.github.xianzhan.http1_1.client.request;

/**
 * 描述：请求方法
 *
 * @author Lee
 * @since 2017/8/3
 */
public enum RequestMethod {

    /**
     * 请求指定的页面信息，并返回实体主体。
     */
    GET("GET"),

    /**
     * 类似于get请求，只不过返回的响应中没有具体的内容，用于获取报头
     */
    HEAD("HEAD"),

    /**
     * 向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。
     * 数据被包含在请求体中。
     * POST请求可能会导致新的资源的建立和/或已有资源的修改。
     */
    POST("POST"),

    /**
     * 从客户端向服务器传送的数据取代指定的文档的内容。
     */
    PUT("PUT"),

    /**
     * 请求服务器删除指定的页面。
     */
    DELETE("DELETE"),

    /**
     * HTTP/1.1协议中预留给能够将连接改为管道方式的代理服务器。
     */
    CONNECT("CONNECT"),

    /**
     * 允许客户端查看服务器的性能。
     */
    OPTIONS("OPTIONS"),

    /**
     * 回显服务器收到的请求，主要用于测试或诊断。
     */
    TRACE("TRACE");

    private String name;

    RequestMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
