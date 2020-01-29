package xianzhan.tank.client.protocol.impl;

import xianzhan.tank.client.TankClient;
import xianzhan.tank.client.bean.Tank;
import xianzhan.tank.client.protocol.IMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 坦克死亡消息
 *
 * @author xianzhan
 * @since 2019-05-06
 */
public class TankDeadMsg implements IMessage {

    private int        tankId;
    private TankClient tc;

    public TankDeadMsg(int tankId) {
        this.tankId = tankId;
    }

    public TankDeadMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int udpPort) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
             DataOutputStream dos = new DataOutputStream(baos)) {

            dos.writeInt(TANK_DEAD_MSG);
            dos.writeInt(tankId);
            byte[] buf = baos.toByteArray();

            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, udpPort));
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parse(DataInputStream dis) {
        try {
            int tankId = dis.readInt();
            if (tankId == this.tc.getMyTank().getId()) {
                return;
            }
            for (Tank t : tc.getTanks()) {
                if (t.getId() == tankId) {
                    t.setLive(false);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
