package xianzhan.core.util;

import java.io.*;
import java.util.Arrays;

/**
 * @author xianzhan
 * @since 2018-06-04
 */
@SuppressWarnings({"unused"})
public class IOUtils {

    /**
     * null 序列化字节数组
     */
    private static final byte[] NULL_BYTES;

    static {
        final int mask = 0xFF;
        final int shift = 8;
        // 5 bytes
        final int length = (Short.BYTES << 1) + Byte.BYTES;
        NULL_BYTES = new byte[length];
        NULL_BYTES[0] = (byte) ((ObjectStreamConstants.STREAM_MAGIC >>> shift) & mask);
        NULL_BYTES[1] = (byte) (ObjectStreamConstants.STREAM_MAGIC & mask);
        NULL_BYTES[2] = (byte) ((ObjectStreamConstants.STREAM_VERSION >>> shift) & mask);
        NULL_BYTES[3] = (byte) (ObjectStreamConstants.STREAM_VERSION & mask);
        NULL_BYTES[4] = ObjectStreamConstants.TC_NULL;
    }

    /**
     * 对象序列化, 支持 null
     *
     * @param obj 序列化对象
     * @return byte[]
     */
    public static byte[] serialize(Object obj) {
        if (obj == null) {
            return NULL_BYTES;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + obj.getClass(), ex);
        }
        return baos.toByteArray();
    }

    /**
     * 反序列化对象, 支持 null
     *
     * @param bytes 反序列化对象
     * @return obj
     */
    public static Object deserialize(byte[] bytes) {
        if (bytes == null || Arrays.equals(bytes, NULL_BYTES)) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        }
    }
}
