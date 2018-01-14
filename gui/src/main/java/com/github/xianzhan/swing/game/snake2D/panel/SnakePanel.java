package com.github.xianzhan.swing.game.snake2D.panel;

import com.github.xianzhan.swing.game.snake2D.snake.Snake;
import com.github.xianzhan.swing.game.snake2D.snake.SnakeEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class SnakePanel extends JPanel implements KeyListener, ActionListener {

    private ImageIcon titleImage;

    private Snake snake;

    private int[] snakeXLength = new int[750];
    private int[] snakeYLength = new int[750];


    private Timer timer;


    private int[] enemyXPos = {
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

    private int[] enemyYPos = {
            75, 100, 125, 150,
            175, 200, 225, 250,
            275, 300, 325, 350,
            375, 400, 425, 450,
            475, 500, 525, 550,
            575, 600, 625
    };

    private ImageIcon enemyImage;
    private Random random = new Random();
    private int xPos = random.nextInt(34);
    private int yPos = random.nextInt(23);


    private int score = 0;

    public SnakePanel() {
        snake = new Snake();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(Snake.SPEED, this);
        timer.start();
    }

    public void paint(Graphics g) {
        if (snake.getMoves() == 0) {
            snakeXLength[2] = 50;
            snakeXLength[1] = 75;
            snakeXLength[0] = 100;

            snakeYLength[2] = 100;
            snakeYLength[1] = 100;
            snakeYLength[0] = 100;
        }

        // draw title image border
        g.setColor(Color.WHITE);
        g.drawRect(24, 10, 851, 55);

        // draw the title image
        titleImage = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\snakeTitle.jpg");
        titleImage.paintIcon(this, g, 25, 11);

        // draw border for gameplay
        g.setColor(Color.WHITE);
        g.drawRect(24, 74, 851, 577);

        // draw background for the gameplay
        g.setColor(Color.BLACK);
        g.fillRect(25, 75, 850, 575);


        // draw scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.PLAIN, 14));
        g.drawString("Scores: " + score, 780, 30);

        // draw length of snack
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.PLAIN, 14));
        g.drawString("Length: " + snake.getLength(), 780, 50);

        snake.paintSite(SnakeEnum.RightMouth, this, g, snakeXLength[0], snakeYLength[0]);

        for (int a = 0; a < snake.getLength(); a++) {
            if (a == 0) {
                if (snake.isRight()) {
                    snake.paintSite(SnakeEnum.RightMouth, this, g, snakeXLength[a], snakeYLength[a]);
                } else if (snake.isLeft()) {
                    snake.paintSite(SnakeEnum.LeftMouth, this, g, snakeXLength[a], snakeYLength[a]);
                } else if (snake.isUp()) {
                    snake.paintSite(SnakeEnum.UpMouth, this, g, snakeXLength[a], snakeYLength[a]);
                } else if (snake.isDown()){
                    snake.paintSite(SnakeEnum.DownMouth, this, g, snakeXLength[a], snakeYLength[a]);
                }
            }

            // body
            if (a != 0) {
                snake.paintSite(SnakeEnum.Body, this, g, snakeXLength[a], snakeYLength[a]);
            }
        }

        enemyImage = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\enemy.png");
        if (enemyXPos[xPos] == snakeXLength[0] && enemyYPos[yPos] == snakeYLength[0]) {
            score++;
            snake.grow();
            xPos = random.nextInt(34);
            yPos = random.nextInt(23);
        }

        enemyImage.paintIcon(this, g, enemyXPos[xPos], enemyYPos[yPos]);


        for (int b = 1; b < snake.getLength(); b++) {
            if (snakeXLength[b] == snakeXLength[0] && snakeYLength[b] == snakeYLength[0]) {
                snake.over();

                g.setColor(Color.WHITE);
                g.setFont(new Font("arial", Font.BOLD, 50));
                g.drawString("Game Over", 300, 300);

                g.setFont(new Font("arial", Font.BOLD, 20));
                g.drawString("Space to RESTART", 350, 340);
            }
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (snake.isRight()) {
            for (int r = snake.getLength() - 1; r >= 0; r--) {
                snakeYLength[r + 1] = snakeYLength[r];
            }
            for (int r = snake.getLength(); r >= 0; r--) {
                if (r == 0) {
                    snakeXLength[r] = snakeXLength[r] + 25;
                } else {
                    snakeXLength[r] = snakeXLength[r - 1];
                }

                if (snakeXLength[r] > 850) {
                    snakeXLength[r] = 25;
                }
            }
            repaint();
        }
        if (snake.isLeft()) {
            for (int l = snake.getLength() - 1; l >= 0; l--) {
                snakeYLength[l + 1] = snakeYLength[l];
            }
            for (int l = snake.getLength(); l >= 0; l--) {
                if (l == 0) {
                    snakeXLength[l] = snakeXLength[l] - 25;
                } else {
                    snakeXLength[l] = snakeXLength[l - 1];
                }

                if (snakeXLength[l] < 25) {
                    snakeXLength[l] = 850;
                }
            }
            repaint();
        }
        if (snake.isUp()) {

            for (int l = snake.getLength() - 1; l >= 0; l--) {
                snakeXLength[l + 1] = snakeXLength[l];
            }
            for (int l = snake.getLength(); l >= 0; l--) {
                if (l == 0) {
                    snakeYLength[l] = snakeYLength[l] - 25;
                } else {
                    snakeYLength[l] = snakeYLength[l - 1];
                }

                if (snakeYLength[l] < 75) {
                    snakeYLength[l] = 625;
                }
            }
            repaint();
        }
        if (snake.isDown()) {
            for (int d = snake.getLength() - 1; d >= 0; d--) {
                snakeXLength[d + 1] = snakeXLength[d];
            }
            for (int d = snake.getLength(); d >= 0; d--) {
                if (d == 0) {
                    snakeYLength[d] = snakeYLength[d] + 25;
                } else {
                    snakeYLength[d] = snakeYLength[d - 1];
                }

                if (snakeYLength[d] > 625) {
                    snakeYLength[d] = 75;
                }
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            snake.init();
            score = 0;
            repaint();
        }

        snake.turn(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
