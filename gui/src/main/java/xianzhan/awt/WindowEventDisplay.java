package xianzhan.awt;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * 窗口事件
 *
 * @author xianzhan
 * @since 2019-08-15
 */
public class WindowEventDisplay extends Frame implements WindowListener {

    private TextField text;

    public WindowEventDisplay() throws HeadlessException {
        setLayout(new FlowLayout());

        add(new Label("Window: "));

        text = new TextField(20);
        text.setEditable(false);
        add(text);

        addWindowListener(this);

        setTitle("Window Event");
        setSize(300, 200);
        setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("windowOpened");
        text.setText("windowOpened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("windowClosing");
        text.setText("windowClosing");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("windowClosed");
        text.setText("windowClosed");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        System.out.println("windowIconified");
        text.setText("windowIconified");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        System.out.println("windowDeiconified");
        text.setText("windowDeiconified");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        System.out.println("windowActivated");
        text.setText("windowActivated");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        System.out.println("windowDeactivated");
        text.setText("windowDeactivated");
    }

    public static void main(String[] args) {
        new WindowEventDisplay();
    }
}
