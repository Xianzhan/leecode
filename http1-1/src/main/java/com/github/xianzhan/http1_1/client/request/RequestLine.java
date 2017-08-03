package com.github.xianzhan.http1_1.client.request;

import com.github.xianzhan.http1_1.core.BlankLine;
import com.github.xianzhan.http1_1.core.ProtocolVersion;
import com.github.xianzhan.http1_1.core.Space;
import com.github.xianzhan.http1_1.core.URL;

/**
 * 描述：请求行
 *
 * @author Lee
 * @since 2017/8/2
 */
public class RequestLine {
    private RequestMethod requestMethod;
    private Space space1;
    private URL url;
    private Space space2;
    private ProtocolVersion protocolVersion;
    private BlankLine blankLine;

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Space getSpace1() {
        return space1;
    }

    public void setSpace1(Space space1) {
        this.space1 = space1;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Space getSpace2() {
        return space2;
    }

    public void setSpace2(Space space2) {
        this.space2 = space2;
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public BlankLine getBlankLine() {
        return blankLine;
    }

    public void setBlankLine(BlankLine blankLine) {
        this.blankLine = blankLine;
    }
}
