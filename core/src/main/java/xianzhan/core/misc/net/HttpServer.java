package xianzhan.core.misc.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 描述：
 *
 * @author Lee
 * @since 2018-11-25
 */
public class HttpServer {

    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(8000)) {
            for (; ; ) {
                Socket s = ss.accept();
                InputStream is = s.getInputStream();
                OutputStream os = s.getOutputStream();

                BufferedInputStream bif = new BufferedInputStream(is);
                final int len = 1024;
                byte[] bytes = new byte[len];
                for (int read = bif.read(bytes); read != -1; read = bif.read()) {
                    String kB = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println(kB);
                    if (read < len) {
                        break;
                    }
                }

                String responseStr = "HTTP/1.1 200 OK\r\n"
                                     + "Content-Length: 11\r\n"
                                     + "\r\n"
                                     + "Hello world";
                os.write(responseStr.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
        }
    }

}
