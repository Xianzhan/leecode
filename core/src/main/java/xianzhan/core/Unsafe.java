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
     * 报告通过 putAddress 存储的本机指针的字节大小。
     * 这个值不是 4 就是 8。
     * 请注意，其他基本类型(存储在本机内存块中)的大小完全由它们的信息内容决定。
     *
     * @return 本机指针的字节大小
     * @see #putAddress(long, long)
     */
    public static int addressSize() {
        return unsafe.addressSize();
    }

    /**
     * 报告本机内存页的字节大小。这个值总是2的幂。
     *
     * @return 本机内存页的字节大小
     */
    public static int pageSize() {
        return unsafe.pageSize();
    }

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

    // ------ 对象 ------

    /**
     * 从给定的 Java 变量中获取一个引用值。
     *
     * @param o      从该对象获取
     * @param offset 偏移地址
     * @return o 对象的偏移地址上的对象
     */
    public static Object getObject(Object o, long offset) {
        return unsafe.getObject(o, offset);
    }

    public static void putObject(Object o, long offset, Object x) {
        unsafe.putObject(o, offset, x);
    }

    public static boolean getBoolean(Object o, long offset) {
        return unsafe.getBoolean(o, offset);
    }

    public static void putBoolean(Object o, long offset, boolean x) {
        unsafe.putBoolean(o, offset, x);
    }

    public static byte getByte(Object o, long offset) {
        return unsafe.getByte(o, offset);
    }

    public static void putByte(Object o, long offset, byte x) {
        unsafe.putByte(o, offset, x);
    }

    public static short getShort(Object o, long offset) {
        return unsafe.getShort(o, offset);
    }

    public static void putShort(Object o, long offset, short x) {
        unsafe.putShort(o, offset, x);
    }

    public static char getChar(Object o, long offset) {
        return unsafe.getChar(o, offset);
    }

    public static void putChar(Object o, long offset, char x) {
        unsafe.putChar(o, offset, x);
    }

    public static int getInt(Object o, long offset) {
        return unsafe.getInt(o, offset);
    }

    public static void putInt(Object o, long offset, int x) {
        unsafe.putInt(o, offset, x);
    }

    public static long getLong(Object o, long offset) {
        return unsafe.getLong(o, offset);
    }

    public static void putLong(Object o, long offset, long x) {
        unsafe.putLong(o, offset, x);
    }

    public static float getFloat(Object o, long offset) {
        return unsafe.getFloat(o, offset);
    }

    public static void putFloat(Object o, long offset, float x) {
        unsafe.putFloat(o, offset, x);
    }

    public static double getDouble(Object o, long offset) {
        return unsafe.getDouble(o, offset);
    }

    /**
     * 返回指定静态 field 的内存地址偏移量,在这个类的其他方法中这个值只是被用作一个访问<br>
     * 特定field的一个方式。这个值对于 给定的field是唯一的，并且后续对该方法的调用都应该<br>
     * 返回相同的值。
     *
     * @param f 需要返回偏移量的 field
     * @return 指定field的偏移量
     * @see #getObject(Object, long)
     * @see #getInt(long)
     */
    public static long staticFieldOffset(Field f) {
        return unsafe.staticFieldOffset(f);
    }

    public static long objectFieldOffset(Field f) {
        return unsafe.objectFieldOffset(f);
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
