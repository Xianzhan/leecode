package xianzhan.tank.client.protocol;

import java.io.DataInputStream;
import java.net.DatagramSocket;

/**
 * 应用层协议接口
 *
 * @author xianzhan
 * @since 2019-05-05
 */
public interface IMessage {

    int TANK_NEW_MSG           = 1;
    int TANK_MOVE_MSG          = 2;
    int MISSILE_NEW_MSG        = 3;
    int TANK_DEAD_MSG          = 4;
    int MISSILE_DEAD_MSG       = 5;
    int TANK_ALREADY_EXIST_MSG = 6;
    int TANK_REDUCE_BLOOD_MSG  = 7;

    void send(DatagramSocket ds, String ip, int udpPort);

    void parse(DataInputStream dis);
}
