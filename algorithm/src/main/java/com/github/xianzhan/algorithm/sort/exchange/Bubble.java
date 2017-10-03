package com.github.xianzhan.algorithm.sort.exchange;

import java.util.Arrays;

/**
 * 描述：在要排序的一组数中，对当前还未排好序的范围内的全部数，
 * 自上而下对相邻的两个数依次进行比较和调整，让较大的数往下沉，
 * 较小的往上冒。
 * 即：每当两相邻的数比较后发现它们的排序与排序要求相反时，
 * 就将它们互换。
 *
 * @author Lee
 * @since 2017/10/3
 */
public class Bubble {

    private static void sort(int[] arr) {
        // 最后一位不需要遍历, 每次最大数的存储位
        for (int i = 0; i < arr.length; i++) {
            // 减一理由同上, 减 i 是不需要再次遍历已经排好序的
            for (int j = 0; j < arr.length - 1 - i; j++) {
                // 每次都比较大小, 像泡泡那样最后越来越大
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {43, 547, 12, 5427, 65, 3425, 3467, 45};
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
