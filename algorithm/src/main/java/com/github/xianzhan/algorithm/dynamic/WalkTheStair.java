package com.github.xianzhan.algorithm.dynamic;

/**
 * 描述：走楼梯。有一座高度是 10 级台阶的楼梯，从下往上走，每跨一步只能
 * 向上 1 级或者 2 级台阶。要求用程序求出一共有多少种走法。
 *
 * @author Lee
 * @since 2017/6/5
 */
public class WalkTheStair {

    public static void main(String[] args) {
        int stairs = 10;
        int step = walk(stairs);
        System.out.println("走 10 阶楼梯有 " + step + " 种走法");
    }

    private static int walk(int stairs) {
        if (stairs == 1) {
            return 1;
        }
        if (stairs == 2) {
            return 2;
        }
        return walk(stairs - 1) + walk(stairs - 2);
    }
}
