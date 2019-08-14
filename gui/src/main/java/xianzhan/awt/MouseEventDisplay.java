package xianzhan.awt;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * 鼠标事件
 *
 * @author xianzhan
 * @since 2019-08-14
 */
public class MouseEventDisplay extends Frame implements MouseListener, MouseMotionListener {

    private TextField clickX;
    private TextField clickY;

    private TextField positionX;
    private TextField positionY;

    public MouseEventDisplay() throws HeadlessException {
        setLayout(new FlowLayout());

        add(new Label("X-Click: "));
        clickX = new TextField(10);
        clickX.setEditable(false);
        add(clickX);

        add(new Label("Y-Click: "));
        clickY = new TextField(10);
        clickY.setEditable(false);
        add(clickY);

        add(new Label("X-Position: "));
        positionX = new TextField(10);
        positionX.setEditable(false);
        add(positionX);

        add(new Label("Y-Position: "));
        positionY = new TextField(10);
        positionY.setEditable(false);
        add(positionY);

        addMouseListener(this);
        addMouseMotionListener(this);

        setTitle("MouseEvent Demo");
        setSize(400, 120);
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickX.setText(Integer.toString(e.getX()));
        clickY.setText(Integer.toString(e.getY()));
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        positionX.setText(Integer.toString(e.getX()));
        positionY.setText(Integer.toString(e.getY()));
    }

    public static void main(String[] args) {
        new MouseEventDisplay();
    }
}
