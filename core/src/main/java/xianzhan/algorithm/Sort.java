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

    // ------------------------- 稳定排序 -----------------------------------------

    /**
     * https://zh.wikipedia.org/wiki/%E5%86%92%E6%B3%A1%E6%8E%92%E5%BA%8F
     * <p>
     * 冒泡排序 <br>
     * <p>
     * 一种简单的排序算法。它重复地走访过要排序的数列，
     * 一次比较两个元素，如果他们的顺序错误就把他们交换过来。
     * 走访数列的工作是重复地进行直到没有再需要交换，
     * 也就是说该数列已经排序完成。
     * 这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。
     * </p>
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
     * <p>
     * 定向冒泡排序，鸡尾酒搅拌排序，搅拌排序（也可以视作选择排序的一种变形），
     * 涟漪排序，来回排序或快乐小时排序，是冒泡排序的一种变形。
     * 此算法与冒泡排序的不同处在于排序时是以双向在序列中进行排序。
     * </p>
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
     * https://zh.wikipedia.org/wiki/%E6%8F%92%E5%85%A5%E6%8E%92%E5%BA%8F
     * <p>
     * 插入排序 <br>
     * <p>
     * 一种简单直观的排序算法。它的工作原理是通过构建有序序列，
     * 对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。
     * 插入排序在实现上，通常采用in-place排序
     * （即只需用到 {\displaystyle O(1)} {\displaystyle O(1)}的额外空间的排序），
     * 因而在从后向前扫描过程中，需要反复把已排序元素逐步向后挪位，为最新元素提供插入空间。
     * </p>
     * 分类: 排序算法 - 稳定 <br>
     * 数据结构: 数组 <br>
     * 最坏时间复杂度: O(n^2) <br>
     * 最优时间复杂度: O(n) <br>
     * 平均时间复杂度: O(n^2) <br>
     * 空间复杂度: O(1)
     */
    public static void insertion(int[] arr) {
        int i, j, length = arr.length;
        for (i = 1; i < length; i++) {
            for (j = i; j > 0; j--) {
                if (arr[j - 1] <= arr[j]) {
                    break;
                }
                swap(arr, j, j - 1);
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
