package com.github.xianzhan.swing.game.snake2D.panel;

import javax.swing.*;
import java.awt.*;

class ScorePanel {

    private ImageIcon titleImage;
    private int score;

    ScorePanel() {
        titleImage = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\snakeTitle.jpg");
    }

    public void paint(Component c, Graphics pen) {
        titleImage.paintIcon(c, pen, 25, 11);
    }

    public void paintScore(Graphics pen, int snakeLength) {
        // draw scores
        pen.setColor(Color.WHITE);
        pen.setFont(new Font("arial", Font.PLAIN, 14));
        pen.drawString("Scores: " + score, 780, 30);

        // draw length of snack
        pen.setColor(Color.WHITE);
        pen.setFont(new Font("arial", Font.PLAIN, 14));
        pen.drawString("Length: " + snakeLength, 780, 50);
    }

    public void addScore() {
        score++;
    }

    public void initScore() {
        score = 0;
    }
}
