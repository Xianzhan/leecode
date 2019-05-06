package xianzhan.tank.client.protocol.impl;

import xianzhan.tank.client.TankClient;
import xianzhan.tank.client.bean.Explode;
import xianzhan.tank.client.bean.Missile;
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
 * 坦克生命值减少消息
 *
 * @author xianzhan
 * @since 2019-05-06
 */
public class TankReduceBloodMsg implements IMessage {

    private int        tankId;
    private Missile    m;
    private TankClient tc;

    public TankReduceBloodMsg(int tankId, Missile m) {
        this.tankId = tankId;
        this.m = m;
    }

    public TankReduceBloodMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int udpPort) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
             DataOutputStream dos = new DataOutputStream(baos)) {

            dos.writeInt(TANK_REDUCE_BLOOD_MSG);
            dos.writeInt(tankId);
            dos.writeInt(m.getX() - 20);
            dos.writeInt(m.getY() - 20);
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
            int id = dis.readInt();
            if (id == tc.getMyTank().getId()) {
                return;
            }
            int bX = dis.readInt();
            int bY = dis.readInt();
            this.tc.getExplodes().add(new Explode(bX, bY, this.tc));
            for (Tank t : tc.getTanks()) {
                if (t.getId() == id) {
                    t.setBlood(t.getBlood() - 20);//找到扣血的坦克, 减少20滴血
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
