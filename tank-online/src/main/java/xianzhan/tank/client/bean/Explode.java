package xianzhan.tank.client.bean;

import xianzhan.tank.client.TankClient;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 子弹爆炸
 *
 * @author xianzhan
 * @since 2019-05-06
 */
public class Explode {
    /**
     * 在正式画出爆炸之前先在其他地方画出一次爆炸, 确保爆炸的图片加入到内存中
     */
    private static boolean    init = false;
    private        int        step = 0;
    private        boolean    live = true;
    private        TankClient tc;
    private        int        x, y;

    private static Toolkit tk     = Toolkit.getDefaultToolkit();
    private static Image[] images = {
            tk.getImage(Explode.class.getClassLoader().getResource("images/explode/explode1.png")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/explode/explode2.png")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/explode/explode3.png")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/explode/explode4.png")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/explode/explode5.png")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/explode/explode6.png")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/explode/explode7.png"))
    };

    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!init) {
            // 先在其他地方画一次爆炸
            for (Image image : images) {
                g.drawImage(image, -100, -100, null);
            }
            init = true;
        }
        if (!live) {
            // 爆炸炸完了就从容器移除
            tc.getExplodes().remove(this);
            return;
        }
        if (step == images.length) {
            // 把爆炸数组中的图片都画一次
            live = false;
            step = 0;
            return;
        }
        g.drawImage(images[step++], x, y, null);
    }
}
