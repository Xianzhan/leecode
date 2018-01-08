package com.github.xianzhan.core.util;

/**
 * 系统工具类
 *
 * @author lee
 * @since 2018-01-08
 */
public final class SystemUtils {

    /**
     * 打印当前工作目录
     *
     * @return 当前工作目录的绝对路径
     */
    public static String pwd() {
        return System.getProperty("user.dir");
    }
}
