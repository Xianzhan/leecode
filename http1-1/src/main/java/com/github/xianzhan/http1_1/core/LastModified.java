package com.github.xianzhan.http1_1.core;

/**
 * 描述：文档的最后改动时间。
 * 客户可以通过If-Modified-Since请求头提供一个日期，
 * 该请求将被视为一个条件GET，
 * 只有改动时间迟于指定时间的文档才会返回，
 * 否则返回一个304（Not Modified）状态。
 * Last-Modified也可用setDateHeader方法来设置。
 *
 * @author Lee
 * @since 2017/8/12
 */
public class LastModified {

}
