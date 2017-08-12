package com.github.xianzhan.http1_1.core;

/**
 * 描述：表示浏览器应该在多少时间之后刷新文档，以秒计。
 * 除了刷新当前文档之外，
 * 你还可以通过setHeader("Refresh", "5; URL=http://host/path")
 * 让浏览器读取指定的页面。
 注意这种功能通常是通过设置HTML页面
 HEAD区的＜META HTTP-EQUIV="Refresh" CONTENT="5;URL=http://host/path"＞实现，
 这是因为，自动刷新或重定向对于那些不能使用CGI或Servlet的HTML编写者十分重要。
 但是，对于Servlet来说，直接设置Refresh头更加方便。

 注意Refresh的意义是"N秒之后刷新本页面或访问指定页面"，
 而不是"每隔N秒刷新本页面或访问指定页面"。
 因此，连续刷新要求每次都发送一个Refresh头，
 而发送204状态代码则可以阻止浏览器继续刷新，
 不管是使用Refresh头还是＜META HTTP-EQUIV="Refresh" ...＞。

 注意Refresh头不属于HTTP 1.1正式规范的一部分，
 而是一个扩展，但Netscape和IE都支持它。
 *
 * @author Lee
 * @since 2017/8/12
 */
public class Refresh {

}
