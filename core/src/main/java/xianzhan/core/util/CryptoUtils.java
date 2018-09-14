package xianzhan.core.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public final class CryptoUtils {

    private static String encodeBase64(byte[] content) {
        Base64.Encoder base64Encoder = Base64.getEncoder();
        return base64Encoder.encodeToString(content);
    }

    private static byte[] decodeBase64(byte[] target) {
        Base64.Decoder base64Decode = Base64.getDecoder();
        return base64Decode.decode(target);
    }

    /**
     * 加密 SHA-256
     * @param content 字符串
     * @return 字符串
     */
    public static String encodeSha256(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Applies sha256 to our content
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));

            return encodeHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String encodeHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        var content = "21323A 我";

        String s = encodeBase64(content.getBytes());
        System.out.println(s);
        System.out.println(new String(decodeBase64(s.getBytes())));
    }
}
