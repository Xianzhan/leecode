package xianzhan.algorithm.leetcode;

/**
 * No.852 山脉数组的封顶索引
 *
 * @author xianzhan
 * @implNote https://leetcode-cn.com/problems/peak-index-in-a-mountain-array/description/
 * @since 2018-06-19
 */
public class No852PeakIndexInAMountainArray {
    public int peakIndexInMountainArray(int[] arr) {
        int index = 0;
        int max = arr[index];
        for (int i = 1; i < arr.length; i++) {
            if (max <= arr[i]) {
                max = arr[i];
                index = i;
            } else {
                break;
            }
        }

        return index;
    }

    public static void main(String[] args) {
        No852PeakIndexInAMountainArray solution = new No852PeakIndexInAMountainArray();
        int[] arr = {0, 2, 1, 0};
        System.out.println(solution.peakIndexInMountainArray(arr));
    }
}
