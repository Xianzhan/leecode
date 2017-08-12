package com.github.xianzhan.http1_1.core;

import com.github.xianzhan.http1_1.client.request.RequestMethod;

import java.util.List;

/**
 * 描述：服务器支持哪些方法(如GET/POST)
 *
 * @author Lee
 * @since 2017/8/12
 */
public class Allow {

    private List<RequestMethod> allows;

    public List<RequestMethod> getAllows() {
        return allows;
    }

    public void setAllows(List<RequestMethod> allows) {
        this.allows = allows;
    }
}
