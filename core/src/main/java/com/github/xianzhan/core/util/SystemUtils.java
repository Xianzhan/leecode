package com.github.xianzhan.core.util;

/**
 * 描述：获取系统信息工具类
 *
 * @author Lee
 * @since 2017/9/4
 */
public class SystemUtils {

    private SystemUtils() {
    }

    /**
     * @see System#props
     */
    private static final String JAVA_VERSION       = "java.version";
    private static final String JAVA_VENDOR        = "java.vendor";
    private static final String JAVA_VENDOR_URL    = "java.vendor.url";
    private static final String JAVA_HOME          = "java.home";
    private static final String JAVA_CLASS_VERSION = "java.class.version";
    private static final String JAVA_CLASS_PATH    = "java.class.path";
    private static final String OS_NAME            = "os.name";
    private static final String OS_ARCH            = "os.arch";
    private static final String OS_VERSION         = "os.version";
    private static final String FILE_SEPARATOR     = "file.separator";
    private static final String PATH_SEPARATOR     = "path.separator";
    private static final String LINE_SEPARATOR     = "line.separator";
    private static final String USER_DIR           = "user.dir";
    private static final String USER_HOME          = "user.home";
    private static final String USER_NAME          = "user.name";

    /**
     * 获取 java 的版本号
     *
     * @return Java version number
     */
    public static String getJavaVersion() {
        return System.getProperty(JAVA_VERSION);
    }

    /**
     * 获取 java 的供应商(Oracle)
     *
     * @return Java vendor specific string
     */
    public static String getJavaVendor() {
        return System.getProperty(JAVA_VENDOR);
    }

    /**
     * 获取 java 供应商网站地址
     *
     * @return Java vendor URL
     */
    public static String getJavaVendorUrl() {
        return System.getProperty(JAVA_VENDOR_URL);
    }

    /**
     * 获取 java 的安装目录
     *
     * @return Java installation directory
     */
    public static String getJavaHome() {
        return System.getProperty(JAVA_HOME);
    }

    /**
     * 获取 java class 版本号
     *
     * @return Java class version number
     */
    public static String getJavaClassVersion() {
        return System.getProperty(JAVA_CLASS_VERSION);
    }

    /**
     * 获取 java classpath
     *
     * @return Java classpath
     */
    public static String getJavaClassPath() {
        return System.getProperty(JAVA_CLASS_PATH);
    }

    /**
     * 获取当前系统名
     *
     * @return Operating System Name
     */
    public static String getOsName() {
        return System.getProperty(OS_NAME);
    }

    /**
     * 获取系统架构<br>
     * 'arm', 'arm64', 'ia32', 'mips', 'mipsel', 'ppc', 'ppc64', 's390', 's390x', 'x32', 'x64', 和 'x86'
     *
     * @return Operating System Architecture
     */
    public static String getOsArch() {
        return System.getProperty(OS_ARCH);
    }

    /**
     * 获取操作系统版本号
     *
     * @return Operating System Version
     */
    public static String getOsVersion() {
        return System.getProperty(OS_VERSION);
    }

    /**
     * 获取文件分隔符
     *
     * @return File separator("/" on Unix)
     */
    public static String getFileSeparator() {
        return System.getProperty(FILE_SEPARATOR);
    }

    /**
     * 获取路径分隔符
     *
     * @return Path separator(":" on Unix)
     */
    public static String getPathSeparator() {
        return System.getProperty(PATH_SEPARATOR);
    }

    /**
     * 获取换行符
     *
     * @return Line separator("\n" on Unix)
     */
    public static String getLineSeparator() {
        return System.getProperty(LINE_SEPARATOR);
    }

    /**
     * 获取用户当前的工作目录
     *
     * @return User working directory
     */
    public static String getUserDir() {
        return System.getProperty(USER_DIR);
    }

    /**
     * 获取当前用户的 home 目录
     *
     * @return User home directory
     */
    public static String getUserHome() {
        return System.getProperty(USER_HOME);
    }

    /**
     * 获取当前用户名
     *
     * @return User account name
     */
    public static String getUserName() {
        return System.getProperty(USER_NAME);
    }

    /**
     * 获取当前 Java 剩余可用系统内存
     *
     * @return 剩余字节
     */
    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static void main(String[] args) {
        System.out.println(getFreeMemory());
    }
}
