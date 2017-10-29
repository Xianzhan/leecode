package com.github.xianzhan.algorithm.sort;

/**
 * 描述：排序的一些工具
 *
 * @author Lee
 * @since 2017/10/29
 */
public class SortUtils {

    public static void swap(int[] arr, int a, int b) {
        arr[a] = arr[a] ^ arr[b];
        arr[b] = arr[a] ^ arr[b];
        arr[a] = arr[a] ^ arr[b];
    }
}
