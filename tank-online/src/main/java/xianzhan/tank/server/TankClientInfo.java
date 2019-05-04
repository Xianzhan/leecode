package xianzhan.tank.server;

/**
 * 客户端信息
 *
 * @auther xianzhan
 * @since 2019-05-02
 */
public class TankClientInfo {

    private String ip;
    private int udpPort;
    private int id;

    public TankClientInfo(String ip, int udpPort, int id) {
        this.ip = ip;
        this.udpPort = udpPort;
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "ip='" + ip + '\'' +
                ", udpPort=" + udpPort +
                ", id=" + id +
                '}';
    }
}
