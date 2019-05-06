package xianzhan.tank.client;

import xianzhan.core.util.ThreadUtils;
import xianzhan.tank.client.protocol.IMessage;
import xianzhan.tank.client.protocol.impl.MissileDeadMsg;
import xianzhan.tank.client.protocol.impl.MissileNewMsg;
import xianzhan.tank.client.protocol.impl.TankAlreadyExistMsg;
import xianzhan.tank.client.protocol.impl.TankDeadMsg;
import xianzhan.tank.client.protocol.impl.TankMoveMsg;
import xianzhan.tank.client.protocol.impl.TankNewMsg;
import xianzhan.tank.client.protocol.impl.TankReduceBloodMsg;
import xianzhan.tank.server.TankServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 网络连接
 *
 * @author xianzhan
 * @since 2019-05-06
 */
public class NetClient {
    private TankClient     tc;
    /**
     * 客户端的UDP端口号
     */
    private int            udpPort;
    /**
     * 服务器IP地址
     */
    private String         serverIP;
    /**
     * 服务器转发客户但UDP包的UDP端口
     */
    private int            serverUDPPort;
    /**
     * 服务器监听坦克死亡的UDP端口
     */
    private int            tankDeadUdpPort;
    /**
     * 客户端的UDP套接字
     */
    private DatagramSocket ds = null;

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    NetClient(TankClient tc) {
        this.tc = tc;
        try {
            this.udpPort = getRandomUDPPort();
        } catch (Exception e) {
            // 弹窗提示
            tc.getUdpPortWrongDialog().setVisible(true);
            // 如果选择到了重复的UDP端口号就退出客户端重新选择.
            System.exit(0);
        }
    }

    /**
     * 与服务器进行TCP连接
     *
     * @param ip server IP
     */
    void connect(String ip) {
        serverIP = ip;
        Socket s = null;
        try {
            // 创建 UDP 套接字
            ds = new DatagramSocket(udpPort);
            try {
                // 创建 TCP 套接字
                s = new Socket(ip, TankServer.TCP_PORT);
            } catch (Exception e1) {
                tc.getServerNotStartDialog().setVisible(true);
            }
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            // 向服务器发送自己的UDP端口号
            dos.writeInt(udpPort);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            // 获得自己的id号
            int id = dis.readInt();
            // 获得服务器转发客户端消息的UDP端口号
            this.serverUDPPort = dis.readInt();
            // 获得服务器监听坦克死亡的 UDP 端口
            this.tankDeadUdpPort = dis.readInt();
            // 设置坦克的id号
            tc.getMyTank().setId(id);
            // 根据坦克的id号分配阵营
            tc.getMyTank().setGood((id & 1) == 0);
            System.out.println("connect to server successfully...");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 开启客户端UDP线程, 向服务器发送或接收游戏数据
        ThreadUtils.execute(new UDPThread());

        // 创建坦克出生的消息
        TankNewMsg msg = new TankNewMsg(tc.getMyTank());
        send(msg);
    }

    /**
     * 客户端随机获取UDP端口号
     *
     * @return
     */
    private int getRandomUDPPort() {
        return 55558 + (int) (Math.random() * 9000);
    }

    public void send(IMessage msg) {
        msg.send(ds, serverIP, serverUDPPort);
    }

    public class UDPThread implements Runnable {

        byte[] buf = new byte[1024];

        @Override
        public void run() {
            while (null != ds) {
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                try {
                    ds.receive(dp);
                    parse(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void parse(DatagramPacket dp) {

            try (ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
                 DataInputStream dis = new DataInputStream(bais);) {
                // 获得消息类型
                int msgType = dis.readInt();
                IMessage msg;
                switch (msgType) {
                    // 根据消息的类型调用对应消息的解析方法
                    case IMessage.TANK_NEW_MSG:
                        msg = new TankNewMsg(tc);
                        msg.parse(dis);
                        break;
                    case IMessage.TANK_MOVE_MSG:
                        msg = new TankMoveMsg(tc);
                        msg.parse(dis);
                        break;
                    case IMessage.MISSILE_NEW_MSG:
                        msg = new MissileNewMsg(tc);
                        msg.parse(dis);
                        break;
                    case IMessage.TANK_DEAD_MSG:
                        msg = new TankDeadMsg(tc);
                        msg.parse(dis);
                        break;
                    case IMessage.MISSILE_DEAD_MSG:
                        msg = new MissileDeadMsg(tc);
                        msg.parse(dis);
                        break;
                    case IMessage.TANK_ALREADY_EXIST_MSG:
                        msg = new TankAlreadyExistMsg(tc);
                        msg.parse(dis);
                        break;
                    case IMessage.TANK_REDUCE_BLOOD_MSG:
                        msg = new TankReduceBloodMsg(tc);
                        msg.parse(dis);
                        break;
                    default:
                        // do nothing
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendClientDisconnectMsg() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
             DataOutputStream dos = new DataOutputStream(baos)) {
            // 发送客户端的UDP端口号, 从服务器Client集合中注销
            dos.writeInt(udpPort);
            byte[] buf = baos.toByteArray();

            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, tankDeadUdpPort));
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
