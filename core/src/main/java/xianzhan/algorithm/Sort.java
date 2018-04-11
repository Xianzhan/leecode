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
     * https://zh.wikipedia.org/wiki/%E5%BD%92%E5%B9%B6%E6%8E%92%E5%BA%8F
     * <p>
     * 归并排序
     * <p>
     * 归并操作（merge），也叫归并算法，指的是将两个已经排序的序列合并成一个序列的操作。 <br>
     * 归并排序算法依赖归并操作。<br>
     * <p>
     * 递归法（Top-down） <br>
     * 1. 申请空间，使其大小为两个已经排序序列之和，该空间用来存放合并后的序列 <br>
     * 2. 设定两个指针，最初位置分别为两个已经排序序列的起始位置 <br>
     * 3. 比较两个指针所指向的元素，选择相对小的元素放入到合并空间，并移动指针到下一位置 <br>
     * 4. 重复步骤3直到某一指针到达序列尾 <br>
     * 5. 将另一序列剩下的所有元素直接复制到合并序列尾 <br>
     * <p>
     * 迭代法（Bottom-up） <br>
     * 1. 将序列每相邻两个数字进行归并操作，形成 {\displaystyle ceil(n/2)} {\displaystyle ceil(n/2)}个序列，排序后每个序列包含两/一个元素 <br>
     * 2. 若此时序列数不是1个则将上述序列再次归并，形成 {\displaystyle ceil(n/4)} {\displaystyle ceil(n/4)}个序列，每个序列包含四/三个元素 <br>
     * 3. 重复步骤2，直到所有元素排序完毕，即序列数为1 <br>
     * <p>
     * 分类: 排序算法 - 稳定 <br>
     * 数据结构: 数组 <br>
     * 最坏时间复杂度: θ(n log n) <br>
     * 最优时间复杂度: θ(n) <br>
     * 平均时间复杂度: θ(n log n) <br>
     * 空间复杂度: θ(n) <br>
     */
    public static void merge(int[] arr) {
        // 这里为迭代版
        int len = arr.length;
        int[] result = new int[len];
        int block, start;

        for (block = 1; block < len; block *= 2) {
            for (start = 0; start < len; start += 2 * block) {
                int low = start;
                int mid = (start + block) < len ? (start + block) : len;
                int high = (start + 2 * block) < len ? (start + 2 * block) : len;
                //两个块的起始下标及结束下标
                int start1 = low;
                int start2 = mid;
                //开始对两个block进行归并排序
                while (start1 < mid && start2 < high) {
                    result[low++] = arr[start1] < arr[start2] ? arr[start1++] : arr[start2++];
                }
                while (start1 < mid) {
                    result[low++] = arr[start1++];
                }
                while (start2 < high) {
                    result[low++] = arr[start2++];
                }
            }
            int[] temp = arr;
            arr = result;
            result = temp;
        }
    }

    // -------------------------- 不稳定排序 ---------------------------------------

    /**
     * https://zh.wikipedia.org/wiki/%E9%80%89%E6%8B%A9%E6%8E%92%E5%BA%8F
     * <p>
     * 选择排序
     * <p>
     * 一种简单直观的排序算法。它的工作原理如下。
     * 首先在未排序序列中找到最小（大）元素，
     * 存放到排序序列的起始位置，然后，
     * 再从剩余未排序元素中继续寻找最小（大）元素，
     * 然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。
     * <p>
     * 分类: 排序算法 - 不稳定排序
     * 数据结构: 数组
     * 最坏时间复杂度: O(n^2)
     * 最优时间复杂度: O(n^2)
     * 平均时间复杂度: O(n^2)
     * 空间复杂度: O(n)total, O(1)auxiliary
     */
    public static void selection(int[] arr) {
        int i, j, min, len = arr.length;
        for (i = 0; i < len - 1; i++) {
            // 未排序序列中最小数据数组下标
            min = i;
            for (j = i + 1; j < len; j++) {
                // 在未排序元素中寻找最小元素, 并保存其下标
                if (arr[min] > arr[j]) {
                    min = j;
                }
            }
            // 将最小元素放到已排序序列的末尾
            swap(arr, min, i);
        }
    }

    /**
     * https://zh.wikipedia.org/wiki/%E5%B8%8C%E5%B0%94%E6%8E%92%E5%BA%8F
     * <p>
     * 希尔排序
     * <p>
     * 也称递减增量排序算法，是插入排序的一种更高效的改进版本。希尔排序是非稳定排序算法。
     * <p>
     * 希尔排序是基于插入排序的以下两点性质而提出改进方法的：
     * 1. 插入排序在对几乎已经排好序的数据操作时，效率高，即可以达到线性排序的效率
     * 2. 但插入排序一般来说是低效的，因为插入排序每次只能将数据移动一位
     * <p>
     * 分类: 排序算法 - 不稳定
     * 数据结构: 数组
     * 最坏时间复杂度: 根据步长序列的不同而不同. O(n log^2 n)
     * 最优时间复杂度: O(n)
     * 平均时间复杂度: 根据步长学列的不同而不同
     * 空间复杂度: O(n)
     */
    public static void shell(int[] arr) {
        int gap = 1, i, j, len = arr.length;
        int temp;
        while (gap < len / 3) {
            gap = gap * 3 + 1;
        }
        for (; gap > 0; gap /= 3) {
            for (i = gap; i < len; i++) {
                temp = arr[i];
                for (j = i - gap; j >= 0 && arr[j] > temp; j -= gap) {
                    arr[j + gap] = arr[j];
                }
                arr[j + gap] = temp;
            }
        }
    }

    // ----------------------------  private ----------------------------------

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
