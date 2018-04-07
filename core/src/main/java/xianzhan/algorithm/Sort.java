package xianzhan.algorithm;

/**
 * https://zh.wikipedia.org/wiki/%E6%8E%92%E5%BA%8F%E7%AE%97%E6%B3%95
 * <p>
 * 排序算法
 * <p>
 * 稳定的排序: <br>
 * 冒泡排序（bubble sort）--- O(n^2) <br>
 * 插入排序（insertion sort）--- O(n^2) <br>
 * 鸡尾酒排序（cocktail sort）--- O(n^2) <br>
 * 桶排序（bucket sort）--- O(n); 需要 O(k) 额外空间 <br>
 * 计数排序（counting sort）--- O(n + k); 需要 O(n + k) 额外空间 <br>
 * 归并排序（merge sort）--- O(n log n); 需要 O(n) 额外空间 <br>
 * 原地归并排序 --- O(n log_2 n) 如果使用最佳的现在版本 <br>
 * 二叉排序树排序（binary tree sort）--- O(n log n) 期望时间; O(n^2) 最坏时间; 需要 O(n) 额外空间 <br>
 * 鸽巢排序（pigeonhole sort）--- O(n + k); 需要 O(k) 额外空间 <br>
 * 基数排序（radix sort）--- O(nk); 需要 O(n) 额外空间 <br>
 * 侏儒排序（gnome sort）--- O(n^2) <br>
 * 图书馆排序（library sort）--- O(n log n) 期望时间; O(n^2) 最坏时间; 需要 (1 + E)n 额外空间 <br>
 * 块排序（block sort）--- O(n log n) <br>
 * <p>
 * 不稳定排序: <br>
 * 选择排序（selection sort）--- O(n^2) <br>
 * 希尔排序（shell sort）--- O(n log_2 n) 如果使用最佳的现在版本 <br>
 * Clover排序算法（Clover sort）--- O(n) 期望时间, O(n^2) 最坏情况 <br>
 * 梳排序 --- O(n log n) <br>
 * 堆排序（heap sort）--- O(n log n) <br>
 * 平滑排序（smooth sort）--- O(n log n) <br>
 * 快速排序（quick sort）--- O(n log n) 期望时间, O(n^2) 最坏情况; 对于大的、随机数列表一般相信是最快的已知排序 <br>
 * 内省排序（intro sort）--- O(n log n) <br>
 * 耐心排序（patience sort）--- O(n log n + k) 最坏情况时间, 需要额外的 O(n + k) 空间, 也需要找到最长的递增子序列 <br>
 * <p>
 * 不实用的排序: <br>
 * Bogo 排序 --- O(n * n!),  最坏的情况下期望时间为无穷。 <br>
 * Stupid排序— {\displaystyle O(n^{3})} O(n^{3});递归版本需要 {\displaystyle O(n^{2})} O(n^{2})额外内存 <br>
 * 珠排序（bead sort）— {\displaystyle O(n)} O(n) 或 {\displaystyle O({\sqrt {n}})} O({\sqrt  {n}}),但需要特别的硬件 <br>
 * 煎饼排序— {\displaystyle O(n)} O(n),但需要特别的硬件 <br>
 * 臭皮匠排序（stooge sort）算法简单，但需要约 {\displaystyle n^{2.7}} {\displaystyle n^{2.7}}的时间 <br>
 *
 * @auther xianzhan
 * @sinese 2018-04-07
 */
public class Sort {

    /**
     * https://zh.wikipedia.org/wiki/%E5%86%92%E6%B3%A1%E6%8E%92%E5%BA%8F
     * <p>
     * 冒泡排序 <br>
     * 分类: 排序算法 - 稳定 <br>
     * 数据结构: 数组 <br>
     * 最坏时间复杂: O(n^2) <br>
     * 最优时间复杂: O(n) <br>
     * 平均时间复杂: O(n^2) <br>
     * 空间复杂度: 总共 O(n), 需要辅助空间 O(1)
     */
    public static void bubble(int[] arr) {
        int i, len = arr.length;
        boolean changed;
        do {
            changed = false;
            for (i = 0; i < len - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    changed = true;
                }
            }
        } while (changed);
    }

    /**
     * https://zh.wikipedia.org/wiki/%E9%B8%A1%E5%B0%BE%E9%85%92%E6%8E%92%E5%BA%8F
     * <p>
     * 鸡尾酒排序 <br>
     * 分类: 排序算法 - 稳定 <br>
     * 数据结构: 数组 <br>
     * 最坏时间复杂度: O(n^2) <br>
     * 最优时间复杂度: O(n) <br>
     * 平均时间复杂度: O(n^2) <br>
     */
    public static void cocktail(int[] arr) {
        int i, left = 0, right = arr.length - 1;
        while (left < right) {
            for (i = left; i < right; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                }
            }
            right--;
            for (i = right; i > left; i--) {
                if (arr[i - 1] > arr[i]) {
                    swap(arr, i, i - 1);
                }
            }
        }
    }

    /**
     * 交换
     *
     * @param arr 数组
     * @param i   原位置
     * @param j   目标位置
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
