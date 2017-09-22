package com.github.xianzhan.core.loader;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/8/20
 */
public class JavaLoader {

    /**
     * 扩展类加载器
     *
     * @return
     */
    public static String getExtClassloader() {
        return System.getProperty("java.ext.dirs");
    }

    /**
     * 系统类加载器
     *
     * @return
     */
    public static String getAppClassloader() {
        return System.getProperty("java.class.path");
    }

    public static void main(String[] args) {
        System.out.println(getAppClassloader());
    }
}
