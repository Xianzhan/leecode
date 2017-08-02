package com.github.xianzhan.http1_1.client;

import com.github.xianzhan.http1_1.client.request.RequestData;
import com.github.xianzhan.http1_1.client.request.RequestHeader;
import com.github.xianzhan.http1_1.client.request.RequestLine;
import com.github.xianzhan.http1_1.core.BlankLine;

/**
 * 描述：HTTP 协议, 客户端请求消息
 *
 * @author Lee
 * @since 2017/8/1
 */
public class HTTPRequest {

    private RequestLine requestLine;
    private RequestHeader requestHeader;
    private BlankLine blankLine;
    private RequestData requestData;

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(RequestLine requestLine) {
        this.requestLine = requestLine;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public BlankLine getBlankLine() {
        return blankLine;
    }

    public void setBlankLine(BlankLine blankLine) {
        this.blankLine = blankLine;
    }

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }
}
