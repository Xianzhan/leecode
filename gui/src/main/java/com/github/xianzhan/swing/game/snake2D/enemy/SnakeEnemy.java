package com.github.xianzhan.swing.game.snake2D.enemy;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SnakeEnemy {

    public static int[] enemyXPos = {
            25, 50, 75, 100,
            125, 150, 175, 200,
            225, 250, 275, 300,
            325, 350, 375, 400,
            425, 450, 475, 500,
            525, 550, 575, 600,
            625, 650, 675, 700,
            725, 750, 775, 800,
            825, 850
    };

    public static int[] enemyYPos = {
            75, 100, 125, 150,
            175, 200, 225, 250,
            275, 300, 325, 350,
            375, 400, 425, 450,
            475, 500, 525, 550,
            575, 600, 625
    };

    private static Random random = new Random();
    public static int xPos = random.nextInt(enemyXPos.length);
    public static int yPos = random.nextInt(enemyYPos.length);

    public void generate() {
        xPos = random.nextInt(enemyXPos.length);
        yPos = random.nextInt(enemyYPos.length);
    }

    private ImageIcon enemyImage;

    public SnakeEnemy() {
        enemyImage = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\enemy.png");
    }

    public void paint(Component c, Graphics g) {
        enemyImage.paintIcon(c, g, enemyXPos[xPos], enemyYPos[yPos]);
    }
}
