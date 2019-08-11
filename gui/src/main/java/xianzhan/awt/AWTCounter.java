package xianzhan.awt;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * AWT GUI 框架计数器
 *
 * @author xianzhan
 * @since 2019-08-06
 */
public class AWTCounter extends Frame implements ActionListener {

    private Label     lblCount;
    private TextField tfCount;
    private Button    btnCount;

    private int count;

    public AWTCounter() throws HeadlessException {
        // 设置为 flow 层
        setLayout(new FlowLayout());

        lblCount = new Label("Counter");
        add(lblCount);

        tfCount = new TextField(Integer.toString(count), 10);
        tfCount.setEditable(false);
        add(tfCount);

        btnCount = new Button("Count");
        add(btnCount);

        btnCount.addActionListener(this);

        setTitle("AWT Counter");
        setSize(300, 100);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ++count;
        tfCount.setText(Integer.toString(count));
    }

    public static void main(String[] args) {
        AWTCounter app = new AWTCounter();
    }
}
