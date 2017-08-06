package com.github.xianzhan.http1_1.server;

import com.github.xianzhan.http1_1.core.BlankLine;
import com.github.xianzhan.http1_1.server.response.MessageHeader;
import com.github.xianzhan.http1_1.server.response.ResponseContext;
import com.github.xianzhan.http1_1.server.response.StatusLine;

/**
 * 描述：HTTP 协议, 服务器响应消息
 *
 * @author Lee
 * @since 2017/8/1
 */
public class HTTPResponse {

    private StatusLine statusLine;
    private MessageHeader messageHeader;
    private BlankLine blankLine;
    private ResponseContext responseContext;

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public BlankLine getBlankLine() {
        return blankLine;
    }

    public void setBlankLine(BlankLine blankLine) {
        this.blankLine = blankLine;
    }

    public ResponseContext getResponseContext() {
        return responseContext;
    }

    public void setResponseContext(ResponseContext responseContext) {
        this.responseContext = responseContext;
    }
}
