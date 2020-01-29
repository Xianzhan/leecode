package xianzhan.tank.client.protocol.impl;

import xianzhan.tank.client.TankClient;
import xianzhan.tank.client.bean.Dir;
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
 * 坦克移动信息
 *
 * @author xianzhan
 * @since 2019-05-06
 */
public class TankMoveMsg implements IMessage {

    private int        id;
    private Dir        dir;
    private Dir        ptDir;
    private TankClient tc;
    private int        x, y;

    public TankMoveMsg(int id, int x, int y, Dir dir, Dir ptDir) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.ptDir = ptDir;
    }

    public TankMoveMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int udpPort) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
             DataOutputStream dos = new DataOutputStream(baos)) {

            dos.writeInt(TANK_MOVE_MSG);
            dos.writeInt(id);
            dos.writeInt(dir.ordinal());
            dos.writeInt(ptDir.ordinal());
            dos.writeInt(x);
            dos.writeInt(y);
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
            if (id == this.tc.getMyTank().getId()) {
                return;
            }
            Dir dir = Dir.values()[dis.readInt()];
            Dir ptDir = Dir.values()[dis.readInt()];
            int x = dis.readInt();
            int y = dis.readInt();
            for (Tank t : tc.getTanks()) {
                if (t.getId() == id) {
                    t.setDir(dir);
                    t.setPtDir(ptDir);
                    t.setX(x);
                    t.setY(y);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
