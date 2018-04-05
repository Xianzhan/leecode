package xianzhan.gui.swing.snake2D;


import xianzhan.gui.swing.snake2D.panel.SnakePanel;

import javax.swing.*;
import java.awt.*;

public class SnakeLauncher {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        SnakePanel snakePanel = new SnakePanel();

        frame.setBounds(10, 10, 905, 700);
        frame.setBackground(Color.DARK_GRAY);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(snakePanel);
    }
}
