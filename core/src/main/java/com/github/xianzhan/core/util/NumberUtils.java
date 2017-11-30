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

    /**
     * 判断是否为奇数
     *
     * @param n 待判断的数
     * @return true 为奇数, false 为偶数
     */
    public static boolean isOdd(int n) {
        // n = 2 -> 00000000 00000000 00000000 00000010
        // & 1 -> & 00000000 00000000 00000000 00000001
        // -------> 00000000 00000000 00000000 00000000
        return (n & 1) != 0;
    }

    /**
     * 判断是否为 2 的整数幂
     * <table>
     * <tr>
     * <td>2 的幂</td>
     * <td>8</td>
     * <td>16</td>
     * </tr>
     * <tr>
     * <td>n</td>
     * <td>00001000</td>
     * <td>00010000</td>
     * </tr>
     * <tr>
     * <td>n - 1</td>
     * <td>00000111</td>
     * <td>00001111</td>
     * </tr>
     * <tr>
     * <td> & 结果</td>
     * <td>00000000</td>
     * <td>00000000</td>
     * </tr>
     * </table>
     *
     * @param n 待判断的值
     * @return true 是, false 否
     */
    public static boolean isPowerOf2(int n) {
        return n != 0 && (n & (n - 1)) == 0;
    }



    public static void main(String[] args) {
        System.out.println(isPowerOf2(4));
    }
}
