package xianzhan.core;

import java.lang.reflect.Field;

/**
 * Unsafe
 *
 * @author xianzhan
 * @since 2019-01-13
 */
@SuppressWarnings("unused")
public final class Unsafe {

    /**
     * sun.misc.Unsafe 的单例名称
     *
     * @see sun.misc.Unsafe#getUnsafe()
     */
    private final static String THE_UNSAFE = "theUnsafe";

    private static sun.misc.Unsafe unsafe;

    private static void init() throws NoSuchFieldException, IllegalAccessException {
        Class<sun.misc.Unsafe> clazz = sun.misc.Unsafe.class;
        Field theUnsafe = clazz.getDeclaredField(THE_UNSAFE);
        theUnsafe.setAccessible(true);
        unsafe = (sun.misc.Unsafe) theUnsafe.get(null);
    }

    static {
        try {
            init();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error("无法初始化 Unsafe");
        }
    }

    // ------ 线程 ------

    /**
     * 线程挂起进入 TIMED_WAITING, 线程将一直阻塞直到超时或者中断等条件出现
     *
     * <pre>
     *     long start = System.currentTimeMillis();
     *     park(true, start + 3000L);
     *     long end = System.currentTimeMillis();
     *     // or
     *     long start = System.currentTimeMillis();
     *     park(false, 3000000000L);
     *     long end = System.currentTimeMillis();
     *
     *     System.out.println(end - start);
     * </pre>
     *
     * @param isAbsolute true 时 time 的单位为毫秒秒, false 时 time 的单位为纳秒
     * @param time       时间
     * @see java.util.concurrent.locks.LockSupport
     */
    public static void park(boolean isAbsolute, long time) {
        unsafe.park(isAbsolute, time);
    }

    /**
     * 最好不要在调用 park 前对当前线程调用 unpark<br>
     * 多次调用 unpark 的效果和调用一次 unpark 的效果一样
     *
     * @param thread 线程
     * @see #park(boolean, long)
     */
    public static void unpark(Object thread) {
        unsafe.unpark(thread);
    }

    // ------ 内存 ------

}
