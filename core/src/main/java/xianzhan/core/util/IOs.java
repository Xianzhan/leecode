package xianzhan.core.util;

import xianzhan.core.Unsafe;

import java.io.*;
import java.util.Arrays;

/**
 * java IO 工具类
 *
 * @author xianzhan
 * @since 2018-06-04
 */
@SuppressWarnings({"unused"})
public class IOs {

    private static final int BUF_SIZE = Unsafe.pageSize();

    private static final int OUTSIDE = -1;

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
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + obj.getClass(), ex);
        }
        return os.toByteArray();
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

    /**
     * 复制输入流的数据到输出流
     *
     * @param input  输入流
     * @param output 输出流
     * @return 复制的字节数
     * @throws IOException 如果由于文件末尾以外的任何原因无法读取第一个字节，如果输入流已经关闭，或者发生了其他I/O错误。
     */
    public static long copy(InputStream input, OutputStream output) throws IOException {
        long copySize = 0;
        byte[] buf = new byte[BUF_SIZE];
        int read;
        while ((read = input.read(buf)) != OUTSIDE) {
            output.write(buf);
            copySize += read;
        }
        return copySize;
    }
}
