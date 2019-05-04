package xianzhan.tank.server;

import xianzhan.core.util.ThreadUtils;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

/**
 * 图形启动器
 *
 * @author xianzhan
 * @since 2019-05-04
 */
public class GraphicLauncher extends Frame {

    private static final long serialVersionUID = -372584177067020800L;

    private static final int WIDTH  = 300;
    private static final int HEIGHT = 500;

    private TankServer server;
    private Image      screen;

    public GraphicLauncher(TankServer server) {
        this.server = server;
    }

    @Override
    public void paint(Graphics g) {
        g.drawString("TankClient: ", 30, 50);
        int y = 80;
        for (TankClientInfo info : server.getTankClientInfoList()) {
            g.drawString(info.toString(), 30, y);
            y += 30;
        }
    }

    @Override
    public void update(Graphics g) {
        if (screen == null) {
            screen = super.createImage(WIDTH, HEIGHT);
        }
        Graphics screenGraphics = screen.getGraphics();
        Color color = screenGraphics.getColor();
        screenGraphics.setColor(Color.YELLOW);
        screenGraphics.fillRect(0, 0, WIDTH, HEIGHT);
        screenGraphics.setColor(color);
        paint(screenGraphics);

        g.drawImage(screen, 0, 0, null);
    }

    public void launch() {
        ThreadUtils.execute(server::start);

        super.setTitle("TankServer");
        super.setResizable(false);
        super.setSize(WIDTH, HEIGHT);
        super.setLocation(200, 100);
        super.setBackground(Color.YELLOW);
        super.setVisible(true);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        ThreadUtils.execute(() -> {
            for (; ; ) {
                super.repaint();
                try {
                    TimeUnit.MILLISECONDS.sleep(17);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        TankServer server = new TankServer();
        GraphicLauncher launcher = new GraphicLauncher(server);
        launcher.launch();
    }
}
