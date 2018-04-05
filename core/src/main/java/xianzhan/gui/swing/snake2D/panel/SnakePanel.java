package xianzhan.gui.swing.snake2D.panel;


import xianzhan.gui.swing.snake2D.enemy.SnakeEnemy;
import xianzhan.gui.swing.snake2D.snake.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SnakePanel extends JPanel implements KeyListener, ActionListener {

    private Snake      snake;
    private SnakeEnemy enemy;
    private ScorePanel scorePanel;


    private Timer timer;


    public SnakePanel() {
        snake = new Snake();
        enemy = new SnakeEnemy();
        scorePanel = new ScorePanel();

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


        // draw border for snakePanel
        pen.setColor(Color.WHITE);
        pen.drawRect(24, 74, 851, 577);

        // draw the title image
        scorePanel.paint(this, pen);

        // draw background for the snakePanel
        pen.setColor(Color.BLACK);
        pen.fillRect(25, 75, 850, 575);

        scorePanel.paintScore(pen, snake.getLength());

        snake.paintRightMouth(this, pen);

        snake.paintAll(this, pen);

        if (snake.eatEnemy()) {
            scorePanel.addScore();
            snake.grow();
            enemy.generate();
        }

        enemy.paint(this, pen);


        if (snake.eatSelf()) {

            gameOver(pen);
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
            scorePanel.initScore();
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

    private void gameOver(Graphics pen) {
        pen.setColor(Color.WHITE);
        pen.setFont(new Font("arial", Font.BOLD, 50));
        pen.drawString("Game Over", 300, 300);

        pen.setFont(new Font("arial", Font.BOLD, 20));
        pen.drawString("Space to RESTART", 350, 340);
    }
}
