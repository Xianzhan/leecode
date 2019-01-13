package xianzhan.core;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Unsafe
 *
 * @author xianzhan
 * @since 2019-01-13
 */
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

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        init();

        int arrayBaseOffset = unsafe.arrayBaseOffset(byte[].class);
        System.out.println(arrayBaseOffset);
        var data = new byte[10];
        System.out.println(Arrays.toString(data));

        byte b = 6;
        unsafe.putByte(data, arrayBaseOffset, b);
        unsafe.putByte(data, arrayBaseOffset + 1, b);
        System.out.println(Arrays.toString(data));

    }
}
