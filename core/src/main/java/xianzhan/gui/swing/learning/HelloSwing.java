package xianzhan.gui.swing.learning;

import javax.swing.*;

/**
 * Hello World in swing
 *
 * @author xianzhan
 * @since 2018-08-18
 */
public class HelloSwing {

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Hello Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World on " + Thread.currentThread());
        frame.getContentPane().add(label);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HelloSwing::createAndShowGUI);
        System.out.println("This might well be displayed before the other message.");
    }
}
