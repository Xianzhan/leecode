package com.github.xianzhan.core.util;

import java.util.Base64;

public class CryptoUtils {

    private static String encodeBase64(byte[] content) {
        Base64.Encoder base64Encoder = Base64.getEncoder();
        return base64Encoder.encodeToString(content);
    }

    private static byte[] decodeBase64(byte[] target) {
        Base64.Decoder base64Decode = Base64.getDecoder();
        return base64Decode.decode(target);
    }

    public static void main(String[] args) {
        var content = "21323A 我";

        String s = encodeBase64(content.getBytes());
        System.out.println(s);
        System.out.println(new String(decodeBase64(s.getBytes())));
    }
}
