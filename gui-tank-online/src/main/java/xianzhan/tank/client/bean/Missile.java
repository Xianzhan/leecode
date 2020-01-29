package xianzhan.tank.client.bean;

import xianzhan.tank.client.TankClient;
import xianzhan.tank.client.event.TankHitEvent;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

/**
 * 子弹
 *
 * @author xianzhan
 * @since 2019-05-06
 */
public class Missile {

    public static final int SPEED_X = 10;
    public static final int SPEED_Y = 10;

    private static int total = 10;

    private int        id;
    private TankClient tc;
    private int        tankId;
    private Dir        dir;
    private boolean    live = true;
    private boolean    good;
    private int        x, y;

    private static Toolkit            tk  = Toolkit.getDefaultToolkit();
    private static Image[]            images;
    private static Map<String, Image> map = new HashMap<>();

    static {
        images = new Image[]{
                tk.getImage(Missile.class.getClassLoader()
                                    .getResource("images/missile/m.png")),
                tk.getImage(Missile.class.getClassLoader()
                                    .getResource("images/missile/n.png"))
        };
        map.put("n", images[0]);
        map.put("m", images[1]);
    }

    public Missile(int tankId, int x, int y, boolean good, Dir dir) {
        this.tankId = tankId;
        this.x = x;
        this.y = y;
        this.good = good;
        this.dir = dir;
        this.id = total++;
    }

    public Missile(int tankId, int x, int y, boolean good, Dir dir, TankClient tc) {
        this(tankId, x, y, good, dir);
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!live) {
            tc.getMissiles().remove(this);
            return;
        }
        g.drawImage(good ? map.get("n") : map.get("m"), x, y, null);
        move();
    }

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

        if (x < 0 || y < 0 || x > TankClient.WIDTH || y > TankClient.HEIGHT) {
            live = false;
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, images[0].getWidth(null), images[0].getHeight(null));
    }

    public boolean hitTank(Tank t) {
        if (this.live && t.isLive() && this.good != t.isGood() && this.getRect().intersects(t.getRect())) {
            this.live = false;
            t.actionToTankHitEvent(new TankHitEvent(this));
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTankId() {
        return tankId;
    }

    public void setTankId(int tankId) {
        this.tankId = tankId;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
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

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }
}
