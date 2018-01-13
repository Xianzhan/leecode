package com.github.xianzhan.swing.game.snake2D.snake;

public class SnakeDirection {
    public static final int LEFT = 0x25;
    public static final int UP = 0x26;
    public static final int RIGHT = 0x27;
    public static final int DOWN = 0x28;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public void stopLeftAndRight() {
        left = false;
        right = false;
    }

    public void stopUpAndDown() {
        up = false;
        down = false;
    }
}
