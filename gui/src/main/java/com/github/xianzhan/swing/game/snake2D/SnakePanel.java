package com.github.xianzhan.swing.game.snake2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class SnakePanel extends JPanel implements KeyListener, ActionListener {
    private ImageIcon titleImage;

    private int moves = 0;

    private int[] snakeXLength = new int[750];
    private int[] snakeYLength = new int[750];

    private int lengthOfSnake = 3;

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    private ImageIcon leftMouth;
    private ImageIcon rightMouth;
    private ImageIcon upMouth;
    private ImageIcon downMouth;

    private ImageIcon snakeImage;

    private Timer timer;
    private int delay = 100;


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
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        if (moves == 0) {
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
        g.drawString("Length: " + lengthOfSnake, 780, 50);


        rightMouth = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\rightMouth.png");
        rightMouth.paintIcon(this, g, snakeXLength[0], snakeYLength[0]);

        for (int a = 0; a < lengthOfSnake; a++) {
            if (a == 0 && right) {
                rightMouth = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\rightMouth.png");
                rightMouth.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
            }
            if (a == 0 && left) {
                leftMouth = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\leftMouth.png");
                leftMouth.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
            }
            if (a == 0 && down) {
                downMouth = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\downMouth.png");
                downMouth.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
            }
            if (a == 0 && up) {
                upMouth = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\upMouth.png");
                upMouth.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
            }

            // body
            if (a != 0) {
                snakeImage = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\snakeImage.png");
                snakeImage.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
            }
        }

        enemyImage = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\enemy.png");
        if (enemyXPos[xPos] == snakeXLength[0] && enemyYPos[yPos] == snakeYLength[0]) {
            score++;
            lengthOfSnake++;
            xPos = random.nextInt(34);
            yPos = random.nextInt(23);
        }

        enemyImage.paintIcon(this, g, enemyXPos[xPos], enemyYPos[yPos]);


        for (int b = 1; b < lengthOfSnake; b++) {
            if (snakeXLength[b] == snakeXLength[0] && snakeYLength[b] == snakeYLength[0]) {
                right = false;
                left = false;
                up = false;
                down = false;

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
        if (right) {
            for (int r = lengthOfSnake - 1; r >= 0; r--) {
                snakeYLength[r + 1] = snakeYLength[r];
            }
            for (int r = lengthOfSnake; r >= 0; r--) {
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
        if (left) {
            for (int l = lengthOfSnake - 1; l >= 0; l--) {
                snakeYLength[l + 1] = snakeYLength[l];
            }
            for (int l = lengthOfSnake; l >= 0; l--) {
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
        if (up) {

            for (int l = lengthOfSnake - 1; l >= 0; l--) {
                snakeXLength[l + 1] = snakeXLength[l];
            }
            for (int l = lengthOfSnake; l >= 0; l--) {
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
        if (down) {
            for (int d = lengthOfSnake - 1; d >= 0; d--) {
                snakeXLength[d + 1] = snakeXLength[d];
            }
            for (int d = lengthOfSnake; d >= 0; d--) {
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
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            moves = 0;
            score = 0;
            lengthOfSnake = 3;
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moves++;
            right = true;

            if (!left) {
                right = true;
            } else {
                right = false;
                left = true;
            }
            up = false;
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moves++;
            left = true;

            if (!right) {
                left = true;
            } else {
                left = false;
                right = true;
            }
            up = false;
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            moves++;
            up = true;

            if (!down) {
                up = true;
            } else {
                up = false;
                down = true;
            }
            left = false;
            right = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            moves++;
            down = true;

            if (!up) {
                down = true;
            } else {
                down = false;
                up = true;
            }
            left = false;
            right = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
