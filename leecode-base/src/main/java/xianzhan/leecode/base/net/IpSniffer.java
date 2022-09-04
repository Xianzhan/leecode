package xianzhan.leecode.base.net;

import java.io.IOException;
import java.net.Socket;

/**
 * IP 端口嗅探器
 */
public class IpSniffer {

    private static final int MAX_PORT = 65535;

    public static void scan(String ip, int startPort) {
        for (; startPort < MAX_PORT; startPort++) {
            try (var socket = new Socket(ip, startPort)) {
                System.out.printf("%s:%d is open.%n", ip, startPort);
            } catch (IOException ignore) {

            }
        }
    }

    public static void main(String[] args) {
        scan("localhost", 1);
    }
}
