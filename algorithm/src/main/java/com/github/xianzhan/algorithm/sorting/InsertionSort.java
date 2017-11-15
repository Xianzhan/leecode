package com.github.xianzhan.algorithm.sorting;

import java.util.Arrays;

/**
 * Insertion Sort
 * --------------
 * A sort that uses the insertion of elements in to the list to sort the list.
 * <p>
 * Time Complexity: O(n**2)
 * <p>
 * Space Complexity: O(n) total
 * <p>
 * Stable: Yes
 * <p>
 * Psuedo Code: CLRS. Introduction to Algorithms. 3rd ed.
 */
public class InsertionSort {

    /**
     * Takes a list of integers and sorts them in ascending order. This sorted
     * list is then returned.
     *
     * @param arr A list of integers
     * @return A list of integers
     */
    static int[] sort(int[] arr) {
        for (int i = 1, length = arr.length; i < length; i++) {
            int tmp = arr[i];
            int position = i;
            while (position > 0 && arr[position - 1] > tmp) {
                arr[position] = arr[position - 1];
                position = position - 1;
            }
            arr[position] = tmp;
        }

        return arr;
    }

    public static void main(String[] args) {
        int[] arr = {5, 7, 3, 1, 9, 6, 3, 5, 22};
        System.out.println(Arrays.toString(sort(arr)));
    }
}
