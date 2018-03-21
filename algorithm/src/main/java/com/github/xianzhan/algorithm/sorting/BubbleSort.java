package com.github.xianzhan.algorithm.sorting;

import java.util.Arrays;

/**
 * Bubble Sort
 * -----------
 * A naive sorting that compares and swaps adjacent elements.
 * Time Complexity: O(n**2)
 * Space Complexity: O(1) Auxiliary
 * Stable: Yes
 * Wiki: http://en.wikipedia.org/wiki/Bubble_sort
 */
public class BubbleSort {

    private static int[] sort(int[] arr) {
        for (int i = 0, length = arr.length; i < length; i++) {
            for (int j = 1; j < length - i; j++) {
                if (arr[j] < arr[j - 1]) {
                    arr[j] ^= arr[j - 1];
                    arr[j - 1] ^= arr[j];
                    arr[j] ^= arr[j - 1];
                }
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] arr = {4, 6, 2, 7, 1, 9, 2, 4, 5, 0};
        System.out.println(Arrays.toString(sort(arr)));
    }
}
