package com.github.xianzhan.swing.game.snake2D.snake;

import com.github.xianzhan.swing.game.snake2D.enemy.SnakeEnemy;

import java.awt.*;

public class Snake {

    /**
     * 速度
     */
    public static final int SPEED = 100;

    /**
     * 方向
     */
    private SnakeDirection direction;
    /**
     * 身体
     */
    private SnakeBody body;
    /**
     * 嘴巴
     */
    private SnakeMouth mouth;

    /**
     * 蛇初始长度
     */
    private static final int INITIAL_LENGTH = 3;

    /**
     * 移动
     */
    private int moves;

    /**
     * true 初始化, false 结束
     */
    private boolean state;

    /**
     * 当前蛇长度
     */
    private int lengthOfSnake;

    private int[] snakeXLength = new int[750];
    private int[] snakeYLength = new int[750];

    public void initializeTheLocation() {
        if (getMoves() == 0) {
            snakeXLength[2] = 50;
            snakeXLength[1] = 75;
            snakeXLength[0] = 100;

            snakeYLength[2] = 100;
            snakeYLength[1] = 100;
            snakeYLength[0] = 100;
        }
    }

    /**
     * 打印向右的嘴巴
     *
     * @param c
     * @param pen
     */
    public void paintRightMouth(Component c, Graphics pen) {
        paintSite(SnakeEnum.RightMouth, c, pen, snakeXLength[0], snakeYLength[0]);
    }

    /**
     * 打印自己全身
     *
     * @param c
     * @param pen
     */
    public void paintAll(Component c, Graphics pen) {
        for (int a = 0; a < getLength(); a++) {
            if (a == 0) {
                if (direction.isRight()) {
                    paintSite(SnakeEnum.RightMouth, c, pen, snakeXLength[a], snakeYLength[a]);
                } else if (direction.isLeft()) {
                    paintSite(SnakeEnum.LeftMouth, c, pen, snakeXLength[a], snakeYLength[a]);
                } else if (direction.isUp()) {
                    paintSite(SnakeEnum.UpMouth, c, pen, snakeXLength[a], snakeYLength[a]);
                } else if (direction.isDown()) {
                    paintSite(SnakeEnum.DownMouth, c, pen, snakeXLength[a], snakeYLength[a]);
                }
            }

            // body
            if (a != 0) {
                paintSite(SnakeEnum.Body, c, pen, snakeXLength[a], snakeYLength[a]);
            }
        }
    }

    /**
     * 吃到敌人
     *
     * @return
     */
    public boolean eatEnemy() {
        return SnakeEnemy.enemyXPos[SnakeEnemy.xPos] == snakeXLength[0] && SnakeEnemy.enemyYPos[SnakeEnemy.yPos] == snakeYLength[0];
    }

    /**
     * 吃到自己
     *
     * @return
     */
    public boolean eatSelf() {
        boolean eat = false;
        for (int body = 1; body < lengthOfSnake; body++) {
            eat = snakeXLength[body] == snakeXLength[0] && snakeYLength[body] == snakeYLength[0];
            if (eat) {
                over();
                break;
            }
        }
        return eat;
    }

    public boolean isMoving() {
        boolean moving = direction.isLeft() || direction.isRight() || direction.isUp() || direction.isDown();
        if (moving) {
            if (direction.isRight()) {
                for (int r = getLength() - 1; r >= 0; r--) {
                    snakeYLength[r + 1] = snakeYLength[r];
                }
                for (int r = getLength(); r >= 0; r--) {
                    if (r == 0) {
                        snakeXLength[r] = snakeXLength[r] + 25;
                    } else {
                        snakeXLength[r] = snakeXLength[r - 1];
                    }

                    if (snakeXLength[r] > 850) {
                        snakeXLength[r] = 25;
                    }
                }
            }
            if (direction.isLeft()) {
                for (int l = getLength() - 1; l >= 0; l--) {
                    snakeYLength[l + 1] = snakeYLength[l];
                }
                for (int l = getLength(); l >= 0; l--) {
                    if (l == 0) {
                        snakeXLength[l] = snakeXLength[l] - 25;
                    } else {
                        snakeXLength[l] = snakeXLength[l - 1];
                    }

                    if (snakeXLength[l] < 25) {
                        snakeXLength[l] = 850;
                    }
                }
            }
            if (direction.isUp()) {

                for (int l = getLength() - 1; l >= 0; l--) {
                    snakeXLength[l + 1] = snakeXLength[l];
                }
                for (int l = getLength(); l >= 0; l--) {
                    if (l == 0) {
                        snakeYLength[l] = snakeYLength[l] - 25;
                    } else {
                        snakeYLength[l] = snakeYLength[l - 1];
                    }

                    if (snakeYLength[l] < 75) {
                        snakeYLength[l] = 625;
                    }
                }
            }
            if (direction.isDown()) {
                for (int d = getLength() - 1; d >= 0; d--) {
                    snakeXLength[d + 1] = snakeXLength[d];
                }
                for (int d = getLength(); d >= 0; d--) {
                    if (d == 0) {
                        snakeYLength[d] = snakeYLength[d] + 25;
                    } else {
                        snakeYLength[d] = snakeYLength[d - 1];
                    }

                    if (snakeYLength[d] > 625) {
                        snakeYLength[d] = 75;
                    }
                }
            }
        }
        return moving;
    }

    public Snake() {
        direction = new SnakeDirection();
        mouth = new SnakeMouth();
        body = new SnakeBody();
        lengthOfSnake = INITIAL_LENGTH;
        state = true;
    }

    private void paintSite(SnakeEnum site, Component c, Graphics g, int x, int y) {
        switch (site) {
            case Body:
                body.paint(c, g, x, y);
                break;
            default:
                mouth.paint(site, c, g, x, y);
        }
    }

    public int getLength() {
        return lengthOfSnake;
    }

    public void grow() {
        lengthOfSnake++;
    }

    public int getMoves() {
        return moves;
    }

    public void init() {
        over();
        moves = 0;
        lengthOfSnake = INITIAL_LENGTH;
        state = true;
    }

    public void over() {
        direction.stopLeftAndRight();
        direction.stopUpAndDown();
        state = false;
    }

    /**
     * 根据键盘输入判断转向
     *
     * @param i
     */
    public void turn(int i) {
        if (direction.isLeft() || direction.isRight()) {
            direction.setUp(SnakeDirection.UP == i);
            direction.setDown(SnakeDirection.DOWN == i);
            if (direction.isUp() || direction.isDown()) {
                direction.stopLeftAndRight();
                move();
            }
        } else if (direction.isUp() || direction.isDown()) {
            direction.setLeft(SnakeDirection.LEFT == i);
            direction.setRight(SnakeDirection.RIGHT == i);
            if (direction.isLeft() || direction.isRight()) {
                direction.stopUpAndDown();
                move();
            }
        } else if (state) {
            direction.setRight(SnakeDirection.RIGHT == i);
            if (direction.isRight()) {
                move();
            }
        }
    }

    private void move() {
        moves++;
    }
}
