package com.github.xianzhan.swing.game.snake2D.snake;

import javax.swing.*;
import java.awt.*;

public class SnakeMouth {

    private ImageIcon leftMouth;
    private ImageIcon rightMouth;
    private ImageIcon upMouth;
    private ImageIcon downMouth;

    SnakeMouth() {
        leftMouth = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\leftMouth.png");
        rightMouth = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\rightMouth.png");
        downMouth = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\downMouth.png");
        upMouth = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\upMouth.png");
    }

    public void paint(SnakeEnum site, Component c, Graphics g, int x, int y) {
        switch (site) {
            case LeftMouth:
                leftMouth.paintIcon(c, g, x, y);
                break;
            case RightMouth:
                rightMouth.paintIcon(c, g, x, y);
                break;
            case UpMouth:
                upMouth.paintIcon(c, g, x, y);
                break;
            case DownMouth:
                downMouth.paintIcon(c, g, x, y);
                break;
            default:
        }
    }
}
