package com.github.xianzhan.http1_1.server.response;

import com.github.xianzhan.http1_1.core.HTTPStatusCode;
import com.github.xianzhan.http1_1.core.ProtocolVersion;
import com.github.xianzhan.http1_1.core.Space;

/**
 * 描述：状态行
 *
 * @author Lee
 * @since 2017/8/6
 */
public class StatusLine {

    private ProtocolVersion protocolVersion;
    private Space space;
    private HTTPStatusCode httpStatusCode;

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public HTTPStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HTTPStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

}
