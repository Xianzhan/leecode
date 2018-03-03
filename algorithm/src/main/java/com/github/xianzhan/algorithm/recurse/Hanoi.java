package com.github.xianzhan.algorithm.recurse;

/**
 * 汉诺塔
 *
 * @author lee
 * @since 2018-03-03
 */
public class Hanoi {

    private static void hanoi(int n, char x, char y, char z) {
        // 当 n <= 0 时什么都不用做
        if (n > 0) {

            // 当 n == 2 时, 先将 x 上的圆盘移动到 z 上
            hanoi(n - 1, x, z, y);

            // 当 n == 1 时
            // 只需移动一次, 将 x 的圆盘移动到 y 上则完成
            System.out.printf("%c->%c\t", x, y);

            // 当 n == 2 时, 最后将 z 上的圆盘移动到 y 上
            hanoi(n - 1, z, y, x);
        }
    }

    public static void main(String[] args) {
        hanoi(6, 'x', 'y', 'z');
    }
}
