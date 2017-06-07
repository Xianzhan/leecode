package com.github.xianzhan.algorithm.dynamic;

import java.util.HashMap;

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
        int step1 = walk(stairs);
        int step2 = walk2(stairs, new HashMap<>());
        int step3 = walk3(stairs);
        System.out.println("走 10 阶楼梯有 " + step1 + " 种走法");
        System.out.println("走 10 阶楼梯有 " + step2 + " 种走法");
        System.out.println("走 10 阶楼梯有 " + step3 + " 种走法");
    }

    /**
     * 递归求解
     * O(2^n)
     *
     * @param stairs
     * @return
     */
    private static int walk(int stairs) {
        if (stairs < 1) {
            return 0;
        }
        if (stairs == 1) {
            return 1;
        }
        if (stairs == 2) {
            return 2;
        }
        return walk(stairs - 1) + walk(stairs - 2);
    }

    /**
     * 备忘录算法
     *
     * @param n
     * @param map
     * @return
     */
    private static int walk2(int n, HashMap<Integer, Integer> map) {
        if (n < 1) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }

        if (map.containsKey(n)) {
            return map.get(n);
        } else {
            int value = walk2(n - 1, map) + walk2(n - 2, map);
            map.put(n, value);
            return value;
        }
    }

    /**
     * 动态规划求解
     *
     * @param n
     * @return
     */
    private static int walk3(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }

        int a = 1;
        int b = 2;
        int tmp = 0;

        for (int i = 3; i <= n; i++) {
            tmp = a + b;
            a = b;
            b = tmp;
        }
        return tmp;
    }
}
