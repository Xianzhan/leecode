package xianzhan.swing;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.HeadlessException;

/**
 * Accumulator
 *
 * @author xianzhan
 * @since 2019-08-17
 */
public class SwingAccumulator extends JFrame {

    private JTextField input, output;

    private int sum = 0;

    public SwingAccumulator() throws HeadlessException {
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 2, 5, 5));

        contentPane.add(new JLabel("Enter an Integer: "));
        input = new JTextField(10);
        contentPane.add(input);

        contentPane.add(new JLabel("The Accumulated Sum is: "));
        output = new JTextField(10);
        output.setEditable(false);
        contentPane.add(output);

        input.addActionListener(e -> {
            String text = input.getText();
            int i = Integer.parseInt(text);
            sum += i;
            input.setText("");
            output.setText(Integer.toString(sum));
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Swing Accumulator");
        setSize(350, 100);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingAccumulator::new);
    }
}
