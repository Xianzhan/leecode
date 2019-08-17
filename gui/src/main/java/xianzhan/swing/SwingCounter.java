package xianzhan.swing;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

/**
 * 计数器
 *
 * @author xianzhan
 * @since 2019-08-15
 */
public class SwingCounter extends JFrame {

    private JTextField text;
    private JButton    button;

    private int count = 0;

    public SwingCounter() throws HeadlessException {
        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        container.add(new JLabel("Counter"));
        text = new JTextField("0", 3);
        text.setEditable(false);
        container.add(text);

        button = new JButton("Count");
        container.add(button);

        button.addActionListener(e -> {
            this.count++;
            text.setText(Integer.toString(this.count));
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Swing Counter");
        setSize(300, 100);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingCounter::new);
    }
}
