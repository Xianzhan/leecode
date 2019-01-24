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

    // ------ 内存 ------

    /**
     * 分配 bytes 字节大小的 "堆外内存", 返回起始地址的偏移量
     *
     * @param bytes 字节
     * @return 偏移量
     * @see #reallocateMemory(long, long)
     * @see #freeMemory(long)
     */
    public static long allocateMemory(long bytes) {
        return unsafe.allocateMemory(bytes);
    }

    /**
     * 重新给 address 起始地址的内存分配长度为 bytes 字节大小的内存, 返回新的内存地址偏移量
     *
     * @param address 起始地址
     * @param bytes   内存大小
     * @return 新的内存起始地址偏移量
     * @see #allocateMemory(long)
     * @see #freeMemory(long)
     */
    public static long reallocateMemory(long address, long bytes) {
        return unsafe.reallocateMemory(address, bytes);
    }

    /**
     * 释放起始地址为 address 的内存
     *
     * @param address 起始地址
     * @see #allocateMemory(long)
     * @see #reallocateMemory(long, long)
     */
    public static void freeMemory(long address) {
        unsafe.freeMemory(address);
    }

    /**
     * 在 address 起始地址上保存 b 数据
     *
     * @param address 起始地址
     * @param b       数据
     * @see #getByte(long)
     */
    public static void putByte(long address, byte b) {
        unsafe.putByte(address, b);
    }

    /**
     * 在 address 地址上获取一个字节内容
     *
     * @param address 起始地址
     * @return 字节内容
     * @see #putByte(long, byte)
     */
    public static byte getByte(long address) {
        return unsafe.getByte(address);
    }

    public static void putShort(long address, short s) {
        unsafe.putShort(address, s);
    }

    public static short getShort(long address) {
        return unsafe.getShort(address);
    }

    public static void putChar(long address, char c) {
        unsafe.putChar(address, c);
    }

    public static char getChar(long address) {
        return unsafe.getChar(address);
    }

    public static void putInt(long address, int i) {
        unsafe.putInt(address, i);
    }

    public static int getInt(long address) {
        return unsafe.getInt(address);
    }

    public static void putLong(long address, long l) {
        unsafe.putLong(address, l);
    }

    public static long getLong(long address) {
        return unsafe.getLong(address);
    }

    public static void putFloat(long address, float f) {
        unsafe.putFloat(address, f);
    }

    public static float getFloat(long address) {
        return unsafe.getFloat(address);
    }

    public static void putDouble(long address, double d) {
        unsafe.putDouble(address, d);
    }

    /**
     * 从起始地址上获取本地的内存地址
     *
     * @param address 起始地址
     * @return 本地的内存地址
     */
    public static long getAddress(long address) {
        return unsafe.getAddress(address);
    }

    /**
     * 将本机指针存储到给定的内存地址中。
     *
     * @param address 指定的内存地址
     * @param x       本机指针
     */
    public static void putAddress(long address, long x) {
        unsafe.putAddress(address, x);
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

    /**
     * 比较 o 的 offset 处内存位置的值和期望的值, 如果相同则更新. <br>
     * 此更新是不可中断的.<br>
     * CAS 操作有 3 个操作数，内存值 M，预期值 E，新值 U，如果 M == E，则将内存值修改为 B，否则啥都不做。
     *
     * @param o        需要更新的对象
     * @param offset   o 中整型 field 的偏移量
     * @param expected 希望 field 中存在的值
     * @param x        如果期待值 expect 与 field 的当前值相同, 设置 field 的值为这个新值
     * @return 如果 field 的值被更改返回 true
     */
    public static boolean compareAndSwapInt(Object o, long offset, int expected, int x) {
        return unsafe.compareAndSwapInt(o, offset, expected, x);
    }

    public static boolean compareAndSwapLong(Object o, long offset, long expected, long x) {
        return unsafe.compareAndSwapLong(o, offset, expected, x);
    }

    public static boolean compareAndSwapObject(Object o, long offset, Object expected, Object x) {
        return unsafe.compareAndSwapObject(o, offset, expected, x);
    }
}
