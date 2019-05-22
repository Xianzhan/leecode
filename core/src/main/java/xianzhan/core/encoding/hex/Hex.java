package xianzhan.core.encoding.hex;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * hex implements hexadecimal encoding and decoding.
 *
 * @author xianzhan
 * @since 2019-05-08
 */
public class Hex {

    private static final String HEX_TABLE = "0123456789abcdef";

    /**
     * @param n bytes
     * @return the length of an encoding of n source bytes.
     */
    public static int encodedLen(int n) {
        return n << 1;
    }

    public static int encode(byte[] dst, byte[] src) {
        int i = 0;
        for (byte b : src) {
            dst[i] = (byte) HEX_TABLE.charAt((b >> 4) & 0x0F);
            dst[i + 1] = (byte) HEX_TABLE.charAt(b & 0x0F);
            i += 2;
        }
        return src.length << 1;
    }

    public static int decode(byte[] dst, byte[] src) {
        if (src.length % 2 == 1) {
            return -1;
        }
        int i = 0, j = 1;
        for (; j < src.length; j += 2) {
            byte a = fromHexChar(src[j - 1]);
            if (a == -1) {
                return -1;
            }
            byte b = fromHexChar(src[j]);
            if (b == -1) {
                return -1;
            }
            dst[i] = (byte) ((a << 4) | b);
            i++;
        }

        return i;
    }

    private static byte fromHexChar(byte ch) {
        if ('0' <= ch && ch <= '9') {
            return (byte) (ch - '0');
        } else if ('a' <= ch && ch <= 'f') {
            return (byte) (ch - 'a' + 10);
        } else if ('A' <= ch && ch <= 'F') {
            return (byte) (ch - 'A' + 10);
        } else {
            return -1;
        }
    }

    public static String encodeToString(byte[] src) {
        return new String(src, StandardCharsets.UTF_8);
    }

    public static byte[] decodeString(String s) {
        byte[] src = s.getBytes(StandardCharsets.UTF_8);
        decode(src, src);
        return Arrays.copyOf(src, src.length >> 1);
    }

    public static String dump(byte[] data) {
        if (data.length == 0) {
            return "";
        }

        int capacity = (1 + ((data.length - 1) / 16)) * 79;
        StringBuilder buf = new StringBuilder(capacity);
        var dumper = new Dumper(buf);
        dumper.write(data);
        dumper.close();
        return buf.toString();
    }

    private static class Dumper {
        private byte[]        rightChars = new byte[18];
        private byte[]        buf        = new byte[14];
        private int           used;
        private long          n;
        private boolean       closed;
        private StringBuilder w;

        Dumper(StringBuilder w) {
            this.w = w;
        }

        void write(byte[] data) {
            if (closed) {
                return;
            }

            for (byte datum : data) {
                if (used == 0) {
                    buf[0] = (byte) (n >> 24);
                    buf[1] = (byte) (n >> 16);
                    buf[2] = (byte) (n >> 8);
                    buf[3] = (byte) n;

                    byte[] a = Arrays.copyOfRange(buf, 4, 14);
                    byte[] b = Arrays.copyOfRange(buf, 0, 4);
                    encode(a, b);
                    System.arraycopy(a, 0, buf, 4, a.length);
                    System.arraycopy(b, 0, buf, 0, b.length);

                    buf[12] = ' ';
                    buf[13] = ' ';

                    for (int j = 4; j < buf.length; j++) {
                        w.append((char) buf[j]);
                    }
                }

                byte[] b = {datum};
                encode(buf, b);
                buf[2] = ' ';
                int l = 3;
                if (used == 7) {
                    buf[3] = ' ';
                    l = 4;
                } else if (used == 15) {
                    buf[3] = ' ';
                    buf[4] = '|';
                    l = 5;
                }

                for (int j = 0; j < l; j++) {
                    w.append((char) buf[j]);
                }
//                n++;
                rightChars[used] = toChar(datum);
                used++;
                n++;

                if (used == 16) {
                    rightChars[16] = '|';
                    rightChars[17] = '\n';
                    for (byte rightChar : rightChars) {
                        w.append((char) rightChar);
                    }
                    used = 0;
                }
            }
        }

        private byte toChar(byte b) {
            if (b < 32 || b > 126) {
                return '.';
            }
            return b;
        }

        void close() {
            if (closed) {
                return;
            }

            closed = true;
            if (used == 0) {
                return;
            }

            buf[0] = ' ';
            buf[1] = ' ';
            buf[2] = ' ';
            buf[3] = ' ';
            buf[4] = '|';
            int nBytes = used;
            while (used < 16) {
                int l = 3;
                if (used == 7) {
                    l = 4;
                } else if (used == 15) {
                    l = 5;
                }
                for (int i = 0; i < l; i++) {
                    w.append((char) buf[i]);
                }
                used++;
            }
            rightChars[nBytes] = '|';
            rightChars[nBytes + 1] = '\n';
            for (int i = 0; i < nBytes + 2; i++) {
                w.append((char) rightChars[i]);
            }
        }
    }

    public static void main(String[] args) {
        byte[] src = decodeString("48656c6c6f20476f7068657221");
        byte[] dst = new byte[src.length << 1];
        System.out.println(encode(dst, src));
        System.out.println(encodeToString(dst));

        System.out.println("\n\n\n");

        System.out.println(decode(src, dst));
        String d = new String(src, StandardCharsets.UTF_8);
        System.out.println(d);

        System.out.println("\n\n\n");

        byte[] bytes = "Go is an open source programming language.".getBytes(StandardCharsets.UTF_8);
        String dump = dump(bytes);
        System.out.println(dump);
    }
}
