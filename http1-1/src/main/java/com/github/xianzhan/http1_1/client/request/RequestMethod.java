package com.github.xianzhan.http1_1.client.request;

/**
 * 描述：请求方法
 *
 * @author Lee
 * @since 2017/8/3
 */
public enum RequestMethod {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String name;

    RequestMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
