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
 * 旧坦克向新坦克发送存在消息
 *
 * @author xianzhan
 * @since 2019-05-06
 */
public class TankAlreadyExistMsg implements IMessage {
    private Tank       tank;
    private TankClient tc;

    TankAlreadyExistMsg(Tank tank) {
        this.tank = tank;
    }

    public TankAlreadyExistMsg(TankClient tc) {
        this.tc = tc;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int udpPort) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
             DataOutputStream dos = new DataOutputStream(baos)) {

            dos.writeInt(TANK_ALREADY_EXIST_MSG);
            dos.writeInt(tank.getId());
            dos.writeInt(tank.getX());
            dos.writeInt(tank.getY());
            dos.writeInt(tank.getDir().ordinal());
            dos.writeBoolean(tank.isGood());
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
            boolean exist = false;
            for (Tank t : tc.getTanks()) {
                if (id == t.getId()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                int x = dis.readInt();
                int y = dis.readInt();
                Dir dir = Dir.values()[dis.readInt()];
                boolean good = dis.readBoolean();
                Tank existTank = new Tank(x, y, good, dir, tc);
                existTank.setId(id);
                tc.getTanks().add(existTank);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
