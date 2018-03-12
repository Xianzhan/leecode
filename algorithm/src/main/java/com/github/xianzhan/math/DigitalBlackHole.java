package com.github.xianzhan.math;

import java.util.Arrays;
import java.util.Random;

/**
 * 任意选一个四位数（数字不能全相同），把所有数字从大到小排列，再把所有数字从小到大排列，
 * 用前者减去后者得到一个新的数。重复对新得到的数进行上述操作，7 步以内必然会得到 6174。
 * <p>
 * 例如，选择四位数 6767：
 * 7766 - 6677 = 1089
 * 9810 - 0189 = 9621
 * 9621 - 1269 = 8352
 * 8532 - 2358 = 6174
 * 7641 - 1467 = 6174
 * ...
 * <p>
 * 6174 这个“黑洞”就叫做 <strong>卡普雷卡尔（Kaprekar）常数</strong>。
 * 对于三位数，也有一个数字黑洞——495。
 *
 * @author lee
 * @since 2018-03-11
 */
public class DigitalBlackHole {

    private static final int TARGET = 6174;

    private static Random r = new Random(System.currentTimeMillis());

    private static int[] nums = new int[4];

    private static int getThousands() {
        float nextFloat;
        int thousands;
        do {
            nextFloat = r.nextFloat();
            thousands = (int) (nextFloat * 10000);
        } while (thousands < 1000);

        System.out.println("生成数字: " + thousands);
        return thousands;
    }

    private static void array(int thousands) {
        nums[0] = thousands / 1000;
        nums[1] = thousands / 100 % 10;
        nums[2] = thousands / 10 % 10;
        nums[3] = thousands % 10;
        Arrays.sort(nums);
    }

    private static int getMax() {
        int sum = 0;
        for (int i = 0, unit = 1; i < nums.length; i++, unit *= 10) {
            sum += nums[i] * unit;
        }
        return sum;
    }

    private static int getMin() {
        int sum = 0;
        for (int i = 0, unit = 1000; i < nums.length; i++, unit /= 10) {
            sum += nums[i] * unit;
        }
        return sum;
    }

    public static void main(String[] args) {
        int thousands = getThousands();
        int count = 0;
        while (thousands != TARGET) {
            count++;
            array(thousands);
            thousands = getMax() - getMin();
            System.out.println(thousands);
        }
        System.out.println("共计算 " + count + " 次找到 " + TARGET);
    }
}
