package xianzhan.tank.client;

import xianzhan.core.util.ThreadUtils;
import xianzhan.tank.client.bean.Dir;
import xianzhan.tank.client.bean.Explode;
import xianzhan.tank.client.bean.Missile;
import xianzhan.tank.client.bean.Tank;
import xianzhan.tank.client.protocol.impl.MissileDeadMsg;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 坦克
 *
 * @author xianzhan
 * @since 2019-05-05
 */
public class TankClient extends Frame {

    public static final int WIDTH  = 800;
    public static final int HEIGHT = 600;

    private Image offScreenImage = null;

    /**
     * 客户端的坦克
     */
    private Tank myTank = new Tank(
            50 + (int) (Math.random() * (WIDTH - 100)),
            50 + (int) (Math.random() * (HEIGHT - 100)),
            true,
            Dir.STOP,
            this
    );

    private NetClient            nc                   = new NetClient(this);
    private ConDialog            dialog               = new ConDialog();
    private GameOverDialog       gameOverDialog       = new GameOverDialog();
    private UDPPortWrongDialog   udpPortWrongDialog   = new UDPPortWrongDialog();
    private ServerNotStartDialog serverNotStartDialog = new ServerNotStartDialog();

    /**
     * 存储游戏中的子弹集合
     */
    private List<Missile> missiles = new CopyOnWriteArrayList<>();
    /**
     * 爆炸集合
     */
    private List<Explode> explodes = new CopyOnWriteArrayList<>();
    /**
     * 坦克集合
     */
    private List<Tank>    tanks    = new CopyOnWriteArrayList<>();

    @Override
    public void paint(Graphics g) {
        g.drawString("missiles count:" + missiles.size(), 10, 50);
        g.drawString("explodes count:" + explodes.size(), 10, 70);
        g.drawString("tanks    count:" + tanks.size(), 10, 90);

        for (Missile m : missiles) {
            if (m.hitTank(myTank)) {
                MissileDeadMsg deadMsg = new MissileDeadMsg(m.getTankId(), m.getId());
                nc.send(deadMsg);
            }
            m.draw(g);
        }
        for (Explode e : explodes) {
            e.draw(g);
        }
        for (Tank t : tanks) {
            t.draw(g);
        }
        myTank.draw(g);
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(800, 600);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.LIGHT_GRAY);
        gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    private void launchFrame() {
        this.setLocation(400, 300);
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("TankWar");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 关闭窗口前要向服务器发出注销消息.
                nc.sendClientDisconnectMsg();
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.LIGHT_GRAY);

        this.addKeyListener(new KeyMonitor());

        this.setVisible(true);

        ThreadUtils.execute(new PaintThread());

        dialog.setVisible(true);
    }

    /**
     * 重画线程
     */
    class PaintThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class KeyMonitor extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }

    }

    /**
     * 游戏开始前连接到服务器的对话框
     */
    class ConDialog extends Dialog {

        /**
         * 服务器的IP地址
         */
        TextField tfIP = new TextField("127.0.0.1", 15);
        Button    b    = new Button("connect to server");

        public ConDialog() {
            super(TankClient.this, true);
            this.setLayout(new FlowLayout());
            this.add(new Label("Server IP:"));
            this.add(tfIP);
            this.add(b);
            this.setLocation(500, 400);
            this.pack();
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    setVisible(false);
                    System.exit(0);
                }
            });
            b.addActionListener(e -> {
                String ip = tfIP.getText().trim();
                nc.connect(ip);
                setVisible(false);
            });
        }

    }

    /**
     * 坦克死亡后退出的对话框
     */
    class GameOverDialog extends Dialog {

        Button b = new Button("exit");

        public GameOverDialog() {
            super(TankClient.this, true);
            this.setLayout(new FlowLayout());
            this.add(new Label("Game Over~"));
            this.add(b);
            this.setLocation(500, 400);
            this.pack();
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            b.addActionListener(e -> System.exit(0));
        }

    }

    /**
     * UDP端口分配失败后的对话框
     */
    class UDPPortWrongDialog extends Dialog {

        Button b = new Button("ok");

        UDPPortWrongDialog() {
            super(TankClient.this, true);
            this.setLayout(new FlowLayout());
            this.add(new Label("something wrong, please connect again"));
            this.add(b);
            this.setLocation(500, 400);
            this.pack();
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            b.addActionListener(e -> System.exit(0));
        }

    }

    /**
     * 连接服务器失败后的对话框
     */
    class ServerNotStartDialog extends Dialog {

        Button b = new Button("ok");

        ServerNotStartDialog() {
            super(TankClient.this, true);
            this.setLayout(new FlowLayout());
            this.add(new Label("The server has not been opened yet..."));
            this.add(b);
            this.setLocation(500, 400);
            this.pack();
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            b.addActionListener(e -> System.exit(0));
        }

    }

    public void gameOver() {
        this.gameOverDialog.setVisible(true);
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    public void setMissiles(List<Missile> missiles) {
        this.missiles = missiles;
    }

    public List<Explode> getExplodes() {
        return explodes;
    }

    public void setExplodes(List<Explode> explodes) {
        this.explodes = explodes;
    }

    public List<Tank> getTanks() {
        return tanks;
    }

    public void setTanks(List<Tank> tanks) {
        this.tanks = tanks;
    }

    public Tank getMyTank() {
        return myTank;
    }

    public void setMyTank(Tank myTank) {
        this.myTank = myTank;
    }

    public NetClient getNc() {
        return nc;
    }

    public void setNc(NetClient nc) {
        this.nc = nc;
    }

    public UDPPortWrongDialog getUdpPortWrongDialog() {
        return udpPortWrongDialog;
    }

    public ServerNotStartDialog getServerNotStartDialog() {
        return serverNotStartDialog;
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }
}
