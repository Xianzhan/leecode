package com.github.xianzhan.http1_1.core;

/**
 * 描述：客户应该在Authorization头中提供什么类型的授权信息？在包含401（Unauthorized）
 * 状态行的应答中这个头是必需的。
 * 例如，response.setHeader("WWW-Authenticate", "BASIC realm=＼"executives＼"")。
 * 注意Servlet一般不进行这方面的处理，
 * 而是让Web服务器的专门机制来控制受密码保护页面的访问（例如.htaccess）。
 *
 * @author Lee
 * @since 2017/8/12
 */
public class WWWAuthenticate {

}
