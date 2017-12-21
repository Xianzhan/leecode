package com.github.xianzhan.core.vm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeTest {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        Object obj = Integer.valueOf("10000");
        int x = 10;
        for (int i = 0; i < x; i++) {
            System.out.println(unsafe.getInt(obj, i));
        }
    }
}
