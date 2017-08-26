package com.github.xianzhan.algorithm.sort.insert;

import java.util.Arrays;

/**
 * 描述：直接插入排序
 * 基本思想：在要排序的一组数中，假设前面(n-1)[n>=2] 个数已经是排好顺序的，
 * 现在要把第n个数插到前面的有序数中，使得这n个数也是排好顺序的。
 * 如此反复循环，直到全部排好顺序。
 *
 * @author Lee
 * @since 2017/8/26
 */
public class Direct {

    static void sort(int[] arr) {
        int location; // 升序数组最后一位的位置
        int flag; // 旗帜

        // 第一位作为长度为 1 且为升序的数组, 不需要动
        for (int i = 1; i < arr.length; i++) {
            location = i - 1;
            flag = arr[i];

            // 从后往前遍历
            for (; location >= 0; location--) {

                // 大于旗帜的数往后移
                if (flag < arr[location]) {
                    arr[location + 1] = arr[location];
                } else { // 找到插入位置
                    break;
                }
            }

            // 最后, 所有大于 flag 的数都往后移了
            // (可能 flag 比 arr[0] 还小)
            // 即最坏的打算, 复杂度为 O(n)
            // n 为升序数组的长度
            arr[location + 1] = flag;
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[]{3, 1, 6, 8, 3, 6, 99, 342, 43, 5};
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
