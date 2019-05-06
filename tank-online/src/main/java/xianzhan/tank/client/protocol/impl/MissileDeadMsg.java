package xianzhan.tank.client.protocol.impl;

import xianzhan.tank.client.TankClient;
import xianzhan.tank.client.bean.Explode;
import xianzhan.tank.client.bean.Missile;
import xianzhan.tank.client.protocol.IMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 子弹死亡消息
 *
 * @author xianzhan
 * @since 2019-05-05
 */
public class MissileDeadMsg implements IMessage {

    private TankClient tc;
    private int        tankId;
    private int        id;

    public MissileDeadMsg(int tankId, int id) {
        this.tankId = tankId;
        this.id = id;
    }

    public MissileDeadMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int udpPort) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
             DataOutputStream dos = new DataOutputStream(baos)) {

            dos.writeInt(MISSILE_DEAD_MSG);
            dos.writeInt(tankId);
            dos.writeInt(id);

            byte[] buf = baos.toByteArray();

            DatagramPacket packet = new DatagramPacket(
                    buf,
                    buf.length,
                    new InetSocketAddress(ip, udpPort)
            );
            ds.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parse(DataInputStream dis) {
        try {
            int tankId = dis.readInt();
            int id = dis.readInt();
            for (Missile m : tc.getMissiles()) {
                if (tankId == tc.getMyTank().getId() && id == m.getId()) {
                    m.setLive(false);
                    tc.getExplodes().add(new Explode(m.getX() - 20, m.getY() - 20, tc));
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
