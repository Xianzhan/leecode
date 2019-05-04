package xianzhan.tank.server;

import xianzhan.core.util.ThreadUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * 坦克服务端
 * <p>
 * 保存客户端信息，转发客户端信息
 *
 * @auther xianzhan
 * @since 2019-05-02
 */
public class TankServer {

    private static final int TCP_PORT  = 9527;
    private static final int UDP_PORT  = 9528;
    private static final int DEAD_PORT = 9529;

    /**
     * 客户端 ID 初始值
     */
    private static int id = 101;

    private List<TankClientInfo> clients = new ArrayList<>();

    /**
     * 启动一线程监听
     */
    public void start() {
        ThreadUtils.execute(this::forward);
        ThreadUtils.execute(this::dead);

        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {

            for (; ; ) {
                Socket s = serverSocket.accept();
                System.out.println("A client has connected...");

                DataInputStream dis = new DataInputStream(s.getInputStream());
                int clientUdpPort = dis.readInt();
                String hostAddress = s.getInetAddress().getHostAddress();
                TankClientInfo info = new TankClientInfo(hostAddress, clientUdpPort, id++);
                clients.add(info);

                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(id);
                dos.writeInt(UDP_PORT);
                dos.writeInt(DEAD_PORT);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 启动一线程转发客户端发送的信息逻辑
     */
    private void forward() {
        byte[] buf = new byte[300];

        try (DatagramSocket ds = new DatagramSocket(UDP_PORT)) {

            for (; ; ) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    ds.receive(packet);
                    for (TankClientInfo info : clients) {
                        packet.setSocketAddress(new InetSocketAddress(info.getIp(), info.getUdpPort()));
                        ds.send(packet);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (SocketException e) {
            System.err.println("Failed to create UDP for forward");
        }
    }

    /**
     * 启动一线程监听客户端死亡的逻辑
     */
    private void dead() {
        byte[] buf = new byte[300];

        try (DatagramSocket ds = new DatagramSocket(DEAD_PORT)) {


            for (; ; ) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                ByteArrayInputStream bais;
                DataInputStream dis;

                try {
                    ds.receive(packet);
                    bais = new ByteArrayInputStream(buf, 0, packet.getLength());
                    dis = new DataInputStream(bais);
                    int deadTankUdpPort = dis.readInt();
                    for (TankClientInfo tankClientInfo : clients) {
                        if (tankClientInfo.getUdpPort() == deadTankUdpPort) {
                            clients.remove(tankClientInfo);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (SocketException e) {
            System.err.println("Failed to create UDP for dead");
        }
    }

    public List<TankClientInfo> getTankClientInfoList() {
        return clients;
    }
}
