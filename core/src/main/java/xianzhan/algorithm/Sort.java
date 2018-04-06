package xianzhan.algorithm;


import java.util.Random;

/**
 * 排序算法
 *
 * @auther xianzhan
 * @sinese 2018-04-07
 */
public class Sort {

    /**
     * https://zh.wikipedia.org/wiki/%E5%86%92%E6%B3%A1%E6%8E%92%E5%BA%8F
     * <p>
     * 分类: 排序算法 <br>
     * 数据结构: 数组 <br>
     * 最坏时间复杂: O(n^2) <br>
     * 最优时间复杂: O(n) <br>
     * 平均时间复杂: O(n^2) <br>
     * 空间复杂度: 总共 O(n), 需要辅助空间 O(1)
     */
    public static void bubble(int[] arr) {
        int i, temp, len = arr.length;
        boolean changed;
        do {
            changed = false;
            for (i = 0; i < len - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    changed = true;
                }
            }
        } while (changed);
    }

    public static int[] arr() {
        return new Random().ints(10, 1, 100).toArray();
    }
}
