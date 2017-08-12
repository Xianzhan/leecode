package com.github.xianzhan.http1_1.server.response;

import com.github.xianzhan.http1_1.core.*;

import java.util.Date;

/**
 * 描述：消息报头
 *
 * @author Lee
 * @since 2017/8/6
 */
public class MessageHeader {

    private Allow allow;
    private ContentEncoding contentEncoding;
    private ContentLength contentLength;
    private ContentType contentType;
    private Date date;
    private Expiress expiress;
    private LastModified lastModified;
    private Location location;
    private Refresh refresh;
    private Server server;
    private SetCookie setCookie;
    private WWWAuthenticate wwwAuthenticate;

    public Allow getAllow() {
        return allow;
    }

    public void setAllow(Allow allow) {
        this.allow = allow;
    }

    public ContentEncoding getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(ContentEncoding contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public ContentLength getContentLength() {
        return contentLength;
    }

    public void setContentLength(ContentLength contentLength) {
        this.contentLength = contentLength;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Expiress getExpiress() {
        return expiress;
    }

    public void setExpiress(Expiress expiress) {
        this.expiress = expiress;
    }

    public LastModified getLastModified() {
        return lastModified;
    }

    public void setLastModified(LastModified lastModified) {
        this.lastModified = lastModified;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Refresh getRefresh() {
        return refresh;
    }

    public void setRefresh(Refresh refresh) {
        this.refresh = refresh;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public SetCookie getSetCookie() {
        return setCookie;
    }

    public void setSetCookie(SetCookie setCookie) {
        this.setCookie = setCookie;
    }

    public WWWAuthenticate getWwwAuthenticate() {
        return wwwAuthenticate;
    }

    public void setWwwAuthenticate(WWWAuthenticate wwwAuthenticate) {
        this.wwwAuthenticate = wwwAuthenticate;
    }
}
