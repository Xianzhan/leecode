package com.github.xianzhan.algorithm.sort.select;

import java.util.Arrays;

/**
 * 描述：在要排序的一组数中，选出最小的一个数与第一个位置的数交换；
 * 然后在剩下的数当中再找最小的与第二个位置的数交换，
 * 如此循环到倒数第二个数和最后一个数比较为止。
 *
 * @author Lee
 * @since 2017/10/3
 */
public class SimpleSelection {

    private static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int min = arr[i]; // 设 arr[i] 为最小值
            int location = i; // 定位最小值位置
            for (int j = i + 1; j < arr.length; j++) {
                // 得到最小值与其位置
                if (min > arr[j]) {
                    min = arr[j];
                    location = j;
                }
            }

            // 交换
            arr[location] = arr[i];
            arr[i] = min;
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 2, 4, 32, 2, 3};
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
