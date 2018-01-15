package com.github.xianzhan.swing.game.snake2D.panel;

import com.github.xianzhan.swing.game.snake2D.enemy.SnakeEnemy;
import com.github.xianzhan.swing.game.snake2D.snake.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SnakePanel extends JPanel implements KeyListener, ActionListener {

    private ImageIcon titleImage;

    private Snake snake;
    private SnakeEnemy enemy;


    private Timer timer;


    private int score = 0;

    public SnakePanel() {
        snake = new Snake();
        enemy = new SnakeEnemy();

        titleImage = new ImageIcon(".\\gui\\src\\main\\resources\\swing\\game\\snake2D\\snakeTitle.jpg");

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(Snake.SPEED, this);
        timer.start();
    }

    @Override
    public void paint(Graphics pen) {

        snake.initializeTheLocation();

        // draw title image border
        pen.setColor(Color.WHITE);
        pen.drawRect(24, 10, 851, 55);

        // draw the title image
        titleImage.paintIcon(this, pen, 25, 11);

        // draw border for snakePanel
        pen.setColor(Color.WHITE);
        pen.drawRect(24, 74, 851, 577);

        // draw background for the snakePanel
        pen.setColor(Color.BLACK);
        pen.fillRect(25, 75, 850, 575);


        // draw scores
        pen.setColor(Color.WHITE);
        pen.setFont(new Font("arial", Font.PLAIN, 14));
        pen.drawString("Scores: " + score, 780, 30);

        // draw length of snack
        pen.setColor(Color.WHITE);
        pen.setFont(new Font("arial", Font.PLAIN, 14));
        pen.drawString("Length: " + snake.getLength(), 780, 50);

        snake.paintRightMouth(this, pen);

        snake.paintAll(this, pen);

        if (snake.eatEnemy()) {
            score++;
            snake.grow();
            enemy.generate();
        }

        enemy.paint(this, pen);


        if (snake.eatSelf()) {

            pen.setColor(Color.WHITE);
            pen.setFont(new Font("arial", Font.BOLD, 50));
            pen.drawString("Game Over", 300, 300);

            pen.setFont(new Font("arial", Font.BOLD, 20));
            pen.drawString("Space to RESTART", 350, 340);
        }

        pen.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (snake.isMoving()) {
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
