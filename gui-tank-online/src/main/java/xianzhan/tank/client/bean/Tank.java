package xianzhan.tank.client.bean;

import xianzhan.tank.client.TankClient;
import xianzhan.tank.client.event.TankHitEvent;
import xianzhan.tank.client.event.TankHitListener;
import xianzhan.tank.client.protocol.impl.MissileNewMsg;
import xianzhan.tank.client.protocol.impl.TankDeadMsg;
import xianzhan.tank.client.protocol.impl.TankMoveMsg;
import xianzhan.tank.client.protocol.impl.TankReduceBloodMsg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xianzhan
 * @since 2019-05-06
 */
public class Tank implements TankHitListener {

    public static final int SPEED_X = 5;
    public static final int SPEED_Y = 5;

    private int        id;
    private boolean    good;
    private boolean    live  = true;
    private TankClient tc;
    private Dir        dir   = Dir.STOP;
    private Dir        ptDir = Dir.D;
    private int        blood = 100;
    private BloodBar   bb    = new BloodBar();
    private int        x, y;
    private boolean bL, bU, bR, bD;

    private static Toolkit            tk  = Toolkit.getDefaultToolkit();
    private static Map<String, Image> map = new HashMap<>();
    private static Image[]            images;

    static {
        images = new Image[]{
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/tD.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/tL.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/tLD.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/tLU.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/tR.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/tRD.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/tRU.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/tU.png")),

                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/eD.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/eL.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/eLD.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/eLU.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/eR.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/eRD.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/eRU.png")),
                tk.getImage(Tank.class.getClassLoader().getResource("images/tank/eU.png"))
        };
        map.put("tD", images[0]);
        map.put("tL", images[1]);
        map.put("tLD", images[2]);
        map.put("tLU", images[3]);
        map.put("tR", images[4]);
        map.put("tRD", images[5]);
        map.put("tRU", images[6]);
        map.put("tU", images[7]);
        map.put("eD", images[8]);
        map.put("eL", images[9]);
        map.put("eLD", images[10]);
        map.put("eLU", images[11]);
        map.put("eR", images[12]);
        map.put("eRD", images[13]);
        map.put("eRU", images[14]);
        map.put("eU", images[15]);
    }

    public static final int WIDTH  = images[0].getWidth(null);
    public static final int HEIGHT = images[0].getHeight(null);

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Dir dir, TankClient tc) {
        this(x, y, good);
        this.dir = dir;
        this.tc = tc;
    }

    /**
     * 根据坦克阵营画出图片
     *
     * @param g
     */
    public void draw(Graphics g) {
        if (!live) {
            if (!good) {
                tc.getTanks().remove(this);
            }
            return;
        }
        switch (ptDir) {
            case L:
                g.drawImage(good ? map.get("tL") : map.get("eL"), x, y, null);
                break;
            case LU:
                g.drawImage(good ? map.get("tLU") : map.get("eLU"), x, y, null);
                break;
            case U:
                g.drawImage(good ? map.get("tU") : map.get("eU"), x, y, null);
                break;
            case RU:
                g.drawImage(good ? map.get("tRU") : map.get("eRU"), x, y, null);
                break;
            case R:
                g.drawImage(good ? map.get("tR") : map.get("eR"), x, y, null);
                break;
            case RD:
                g.drawImage(good ? map.get("tRD") : map.get("eRD"), x, y, null);
                break;
            case D:
                g.drawImage(good ? map.get("tD") : map.get("eD"), x, y, null);
                break;
            case LD:
                g.drawImage(good ? map.get("tLD") : map.get("eLD"), x, y, null);
                break;
            default:
                // do nothing
        }
        g.drawString("id:" + id, x, y - 20);
        // 画出血条
        bb.draw(g);
        move();
    }

    /**
     * 根据坦克的方向进行移动
     */
    private void move() {
        switch (dir) {
            case L:
                x -= SPEED_X;
                break;
            case LU:
                x -= SPEED_X;
                y -= SPEED_Y;
                break;
            case U:
                y -= SPEED_Y;
                break;
            case RU:
                x += SPEED_X;
                y -= SPEED_Y;
                break;
            case R:
                x += SPEED_X;
                break;
            case RD:
                x += SPEED_X;
                y += SPEED_Y;
                break;
            case D:
                y += SPEED_Y;
                break;
            case LD:
                x -= SPEED_X;
                y += SPEED_Y;
                break;
            case STOP:
                break;
            default:
                // do nothing
        }

        if (dir != Dir.STOP) {
            ptDir = dir;
        }

        if (x < 0) {
            x = 0;
        }
        if (y < 30) {
            y = 30;
        }
        if (x + WIDTH > TankClient.WIDTH) {
            x = TankClient.WIDTH - WIDTH;
        }
        if (y + HEIGHT > TankClient.HEIGHT) {
            y = TankClient.HEIGHT - HEIGHT;
        }
    }

    /**
     * 监听键盘按下, 上下左右移动分别对应 W S A D
     *
     * @param e 事件
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A:
                bL = true;
                break;
            case KeyEvent.VK_W:
                bU = true;
                break;
            case KeyEvent.VK_D:
                bR = true;
                break;
            case KeyEvent.VK_S:
                bD = true;
                break;
            default:
                // do nothing
        }
        locateDirection();
    }

    /**
     * 根据4个方向的布尔值判断坦克的方向
     */
    private void locateDirection() {
        Dir oldDir = this.dir;
        if (bL && !bU && !bR && !bD) {
            dir = Dir.L;
        } else if (bL && bU && !bR && !bD) {
            dir = Dir.LU;
        } else if (!bL && bU && !bR && !bD) {
            dir = Dir.U;
        } else if (!bL && bU && bR && !bD) {
            dir = Dir.RU;
        } else if (!bL && !bU && bR && !bD) {
            dir = Dir.R;
        } else if (!bL && !bU && bR) {
            dir = Dir.RD;
        } else if (!bL && !bU && bD) {
            dir = Dir.D;
        } else if (bL && !bU && !bR) {
            dir = Dir.LD;
        } else if (!bL && !bU) {
            dir = Dir.STOP;
        }

        if (dir != oldDir) {
            TankMoveMsg msg = new TankMoveMsg(id, x, y, dir, ptDir);
            tc.getNc().send(msg);
        }
    }

    /**
     * 监听键盘释放
     *
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            // 监听到J键按下则开火
            case KeyEvent.VK_J:
                fire();
                break;
            case KeyEvent.VK_A:
                bL = false;
                break;
            case KeyEvent.VK_W:
                bU = false;
                break;
            case KeyEvent.VK_D:
                bR = false;
                break;
            case KeyEvent.VK_S:
                bD = false;
                break;
            default:
                // do nothing
        }
        locateDirection();
    }

    private Missile fire() {
        // 发出一颗炮弹的方法
        if (!live) {
            return null;
        }
        // 确定子弹的坐标, 这里应该用子弹的常量计算, 待修正
        int x = this.x + 15 - 5;
        int y = this.y + 15 - 5;
        // 产生一颗子弹
        Missile m = new Missile(id, x, y, this.good, this.ptDir, this.tc);
        tc.getMissiles().add(m);

        MissileNewMsg msg = new MissileNewMsg(m);
        tc.getNc().send(msg);
        return m;
    }

    @Override
    public void actionToTankHitEvent(TankHitEvent tankHitEvent) {
        // 坦克自身产生一个爆炸
        this.tc.getExplodes().add(new Explode(tankHitEvent.getSource().getX() - 20,
                                              tankHitEvent.getSource().getY() - 20, this.tc));
        if (this.blood == 20) {
            // 坦克每次扣20滴血, 如果只剩下20滴了, 那么就标记为死亡.
            this.live = false;
            TankDeadMsg msg = new TankDeadMsg(this.id);
            // 向其他客户端转发坦克死亡的消息
            this.tc.getNc().send(msg);
            this.tc.getNc().sendClientDisconnectMsg();//和服务器断开连接
            this.tc.gameOver();
            return;
        }
        // 血量减少20并通知其他客户端本坦克血量减少20.
        this.blood -= 20;
        TankReduceBloodMsg msg = new TankReduceBloodMsg(this.id, tankHitEvent.getSource());
        this.tc.getNc().send(msg);
    }

    /**
     * 血条
     */
    private class BloodBar {
        public void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.BLACK);
            g.drawRect(x, y - 15, 30, 8);
            int w = (30 * blood) / 100;
            g.setColor(Color.RED);
            g.fillRect(x, y - 15, w, 8);
            g.setColor(c);
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, images[0].getWidth(null), images[0].getHeight(null));
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public Dir getPtDir() {
        return ptDir;
    }

    public void setPtDir(Dir ptDir) {
        this.ptDir = ptDir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }
}
