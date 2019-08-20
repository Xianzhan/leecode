package xianzhan.swing;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.net.URL;

/**
 * X O
 *
 * @author xianzhan
 * @since 2019-08-20
 */
public class SwingJComponentSetterTest extends JFrame {

    private String imgCrossFilename  = "images/cross.gif";
    private String imgNoughtFilename = "images/nought.gif";

    public SwingJComponentSetterTest() throws HeadlessException {

        ImageIcon iconCross = null;
        ImageIcon iconNought = null;
        URL imgURL = getClass().getClassLoader().getResource(imgCrossFilename);
        if (imgURL != null) {
            iconCross = new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + imgCrossFilename);
        }
        imgURL = getClass().getClassLoader().getResource(imgNoughtFilename);
        if (imgURL != null) {
            iconNought = new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + imgNoughtFilename);
        }

        Container cp = getContentPane();
        cp.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel label = new JLabel("JLabel", iconCross, SwingConstants.CENTER);
        label.setFont(new Font(Font.DIALOG, Font.ITALIC, 14));
        label.setOpaque(true);
        label.setBackground(new Color(204, 238, 241));
        label.setForeground(Color.RED);
        label.setPreferredSize(new Dimension(120, 80));
        label.setToolTipText("This is a JLabel");
        cp.add(label);

        // Create a JButton with text and icon and set its appearances
        JButton button = new JButton();
        button.setText("Button");
        button.setIcon(iconNought);
        button.setVerticalAlignment(SwingConstants.BOTTOM);
        button.setHorizontalAlignment(SwingConstants.RIGHT);
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        button.setVerticalTextPosition(SwingConstants.TOP);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        button.setBackground(new Color(231, 240, 248));
        button.setForeground(Color.BLUE);
        button.setPreferredSize(new Dimension(150, 80));
        button.setToolTipText("This is a JButton");
        button.setMnemonic(KeyEvent.VK_B);
        cp.add(button);

        // Create a JTextField with text and icon and set its appearances
        JTextField textField = new JTextField("Text Field", 15);
        textField.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 12));
        textField.setForeground(Color.RED);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setToolTipText("This is a JTextField");
        cp.add(textField);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("JComponent Test");
        setLocationRelativeTo(null);
        setSize(500, 150);
        setVisible(true);

        // Print description of the JComponents via toString()
        System.out.println(label);
        System.out.println(button);
        System.out.println(textField);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingJComponentSetterTest::new);
    }
}
