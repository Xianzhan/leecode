package xianzhan.tank.client.protocol.impl;

import xianzhan.tank.client.TankClient;
import xianzhan.tank.client.bean.Dir;
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
 * @author xianzhan
 * @since 2019-05-06
 */
public class MissileNewMsg implements IMessage {

    private TankClient tc;
    private Missile    m;

    public MissileNewMsg(TankClient tc) {
        this.tc = tc;
    }

    public MissileNewMsg(Missile m) {
        this.m = m;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int udpPort) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
             DataOutputStream dos = new DataOutputStream(baos)) {

            dos.writeInt(MISSILE_NEW_MSG);
            dos.writeInt(m.getTankId());
            dos.writeInt(m.getId());
            dos.writeInt(m.getX());
            dos.writeInt(m.getY());
            dos.writeInt(m.getDir().ordinal());
            dos.writeBoolean(m.isGood());
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
            if (tankId == tc.getMyTank().getId()) {
                return;
            }
            int id = dis.readInt();
            int x = dis.readInt();
            int y = dis.readInt();
            Dir dir = Dir.values()[dis.readInt()];
            boolean good = dis.readBoolean();

            Missile m = new Missile(tankId, x, y, good, dir, tc);
            m.setId(id);
            tc.getMissiles().add(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
