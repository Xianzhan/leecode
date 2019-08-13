package xianzhan.awt;

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
 * 累加器
 *
 * @author xianzhan
 * @since 2019-08-13
 */
public class AWTAccumulator extends Frame implements ActionListener {

    private Label lblInput;
    private Label lblOutput;
    private TextField tfInput;
    private TextField tfOutput;

    private int sum;

    public AWTAccumulator() throws HeadlessException {
        setLayout(new FlowLayout());

        lblInput = new Label("Enter an Integer: ");
        add(lblInput);

        tfInput = new TextField(10);
        add(tfInput);
        tfInput.addActionListener(this);

        lblOutput = new Label("The Accumulated Sum is: ");
        add(lblOutput);

        tfOutput = new TextField(10);
        tfOutput.setEditable(false);
        add(tfOutput);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setTitle("AWT Accumulator");
        setSize(350, 120);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int num = Integer.parseInt(tfInput.getText());
        sum += num;
        tfInput.setText("");
        tfOutput.setText(String.valueOf(sum));
    }

    public static void main(String[] args) {
        new AWTAccumulator();
    }
}
