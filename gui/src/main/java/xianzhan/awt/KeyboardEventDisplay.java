package xianzhan.awt;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 键盘事件
 *
 * @author xianzhan
 * @since 2019-08-14
 */
public class KeyboardEventDisplay extends Frame implements KeyListener {

    private TextField input;
    private TextArea  display;

    public KeyboardEventDisplay() throws HeadlessException {
        setLayout(new FlowLayout());

        add(new Label("Enter Text: "));
        input = new TextField(10);
        add(input);
        input.addKeyListener(this);

        display = new TextArea(5, 40);
        add(display);

        setTitle("KeyEvent Demo");
        setSize(400, 200);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        display.append("You have typed " + e.getKeyChar() + '\n');
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        new KeyboardEventDisplay();
    }
}
