package com.github.xianzhan.core.util;

public class NumberUtils {

    /**
     * 将字节数组转为十六进制
     *
     * @param src 字节数组
     * @return String
     */
    public static String toHex(byte[] src) {
        StringBuilder sb = new StringBuilder("");
        if (src != null && src.length > 0) {
            for (int i = 0, length = src.length; i < length; i++) {
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    sb.append(0);
                }
                sb.append(hv);
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        String string = "123456";
        System.out.println(toHex(string.getBytes()));
    }
}
