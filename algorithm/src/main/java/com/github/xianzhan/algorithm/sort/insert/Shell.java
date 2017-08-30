package com.github.xianzhan.algorithm.sort.insert;

/**
 * 描述：希尔排序(最小增量排序)
 * 希尔排序的实质就是分组插入排序，该方法又称缩小增量排序，
 * 因DL．Shell于1959年提出而得名。
 * <p>
 * 该方法的基本思想是：
 * 先将整个待排元素序列分割成若干个子序列
 * （由相隔某个“增量”的元素组成的）分别进行直接插入排序，
 * 然后依次缩减增量再进行排序，待整个序列中的元素基本有序（增量足够小）时，
 * 再对全体元素进行一次直接插入排序。
 * 因为直接插入排序在元素基本有序的情况下（接近最好情况），
 * 效率是很高的。
 *
 * @author Lee
 * @since 2017/8/30
 */
public class Shell {

    public static void sort(int[] arr) {
        int length = arr.length;

        // 分组进行直接插入排序
        // gap 为间隔
        for (int gap = length / 2; gap > 0; gap /= 2) {
            // 从第 i 组开始直接插入排序
            for (int i = 0; i < gap; i++) {
                // i + length 即要出入的数 flag 的位置
                for (int j = i + gap; j < arr.length; j += gap) {
                    int flag = arr[j];
                    int k; // 随后要帮助定位的数
                    // 大于 flag 的数整体往后移
                    for (k = j - gap; k >= 0 && arr[k] > flag; k -= gap) {
                        arr[k + gap] = arr[k];
                    }
                    arr[k + gap] = flag; // 插入
                }
            }
        }
    }
}
