package com.github.xianzhan.swing.game.snake2D.snake;

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

    public Snake() {
        direction = new SnakeDirection();
        mouth = new SnakeMouth();
        body = new SnakeBody();
        lengthOfSnake = INITIAL_LENGTH;
        state = true;
    }

    public void paintSite(SnakeEnum site, Component c, Graphics g, int x, int y) {
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

    public boolean isLeft() {
        return direction.isLeft();
    }

    public boolean isRight() {
        return direction.isRight();
    }

    public boolean isUp() {
        return direction.isUp();
    }

    public boolean isDown() {
        return direction.isDown();
    }

    public void over() {
        direction.stopLeftAndRight();
        direction.stopUpAndDown();
        state = false;
    }

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
