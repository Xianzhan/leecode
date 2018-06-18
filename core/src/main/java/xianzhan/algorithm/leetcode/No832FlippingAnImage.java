package xianzhan.algorithm.leetcode;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * No.832 翻转图像
 *
 * @author xianzhan
 * @apiNote https://leetcode-cn.com/problems/flipping-an-image/description/
 * @since 2018-06-18
 */
public class No832FlippingAnImage {
    public int[][] flipAndInvertImage(int[][] a) {
        flip(a);
        invert(a);
        return a;
    }

    private void invert(int[][] a) {
        Consumer<int[]> invertConsumer = arr -> {
            for (int i = 0; i < arr.length; i++) {
                arr[i] ^= 1;
            }
        };
        foreach(a, invertConsumer);
    }

    private void flip(int[][] a) {
        Consumer<int[]> flipConsumer = arr -> {
            int length = arr.length;
            int half = length >> 1;
            for (int i = 0; i < half; i++) {
                int last = length - i - 1;
                int temp = arr[i];
                arr[i] = arr[last];
                arr[last] = temp;
            }
        };
        foreach(a, flipConsumer);
    }

    private void foreach(int[][] arr, Consumer<int[]> consumer) {
        for (int[] a : arr) {
            consumer.accept(a);
        }
    }

    public static void main(String[] args) {
        No832FlippingAnImage solution = new No832FlippingAnImage();
        int[][] a = {
                {1, 1, 0},
                {1, 0, 1},
                {0, 0, 0}
        };
        solution.foreach(a, arr -> System.out.println(Arrays.toString(arr)));
        System.out.println("------ flipAndInvertImage ------");
        solution.flipAndInvertImage(a);
        solution.foreach(a, arr -> System.out.println(Arrays.toString(arr)));
    }
}
