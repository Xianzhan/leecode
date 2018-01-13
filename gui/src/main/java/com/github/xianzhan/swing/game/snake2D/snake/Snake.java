package com.github.xianzhan.swing.game.snake2D.snake;

public class Snake {

    public static final int SPEED = 100;

    private SnakeDirection direction;

    private static final int INITIAL_LENGTH = 3;

    private int moves;

    private boolean state;

    private int lengthOfSnake;

    public int getLength() {
        return lengthOfSnake;
    }

    public void grow() {
        lengthOfSnake++;
    }

    public int getMoves() {
        return moves;
    }

    public Snake() {
        direction = new SnakeDirection();
        lengthOfSnake = INITIAL_LENGTH;
        state = true;
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
        } else {
            if (state) {
                direction.setRight(SnakeDirection.RIGHT == i);
                if (direction.isRight()) {
                    move();
                }
            }
        }
    }

    private void move() {
        moves++;
    }
}
