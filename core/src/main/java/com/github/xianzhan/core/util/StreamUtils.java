package com.github.xianzhan.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 描述：流工具类
 *
 * @author Lee
 * @since 2017/6/18
 */
public class StreamUtils {

    /**
     * 字节转对象
     *
     * @param bytes
     * @return
     */
    public static Object streamToObjectByJava(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);

            obj = ois.readObject();
            ois.close();
            bais.close();
        } catch (Exception e) {

        }
        return obj;
    }

    public static byte[] objectToBytesByJava(Object obj) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            bytes = baos.toByteArray();
            oos.close();
            baos.close();
        } catch (Exception e) {

        }
        return bytes;
    }
}
