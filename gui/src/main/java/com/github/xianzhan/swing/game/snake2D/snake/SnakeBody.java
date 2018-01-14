package com.github.xianzhan.swing.game.snake2D.snake;

import javax.swing.ImageIcon;
import java.awt.*;

public class SnakeBody {

    private ImageIcon snakeBodyImage;

    public SnakeBody() {
        snakeBodyImage = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\snakeBodyImage.png");
    }

    public void paint(Component c, Graphics g, int x, int y) {
        snakeBodyImage.paintIcon(c, g, x, y);
    }
}
