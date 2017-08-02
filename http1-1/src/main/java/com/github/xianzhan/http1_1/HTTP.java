package com.github.xianzhan.http1_1;

import com.github.xianzhan.http1_1.client.HTTPRequest;
import com.github.xianzhan.http1_1.server.HTTPResponse;

/**
 * 描述：HTTP 协议<br>
 * HTTP协议是Hyper Text Transfer Protocol（超文本传输协议）<br>
 * 的缩写,是用于从万维网（WWW:World Wide Web ）<br>
 * 服务器传输超文本到本地浏览器的传送协议。<br>
 *
 * @author Lee
 * @since 2017/8/1
 */
public class HTTP {

    private HTTPRequest httpRequest;
    private HTTPResponse httpResponse;

    public HTTPRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HTTPRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public HTTPResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HTTPResponse httpResponse) {
        this.httpResponse = httpResponse;
    }
}
