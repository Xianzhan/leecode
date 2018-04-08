package xianzhan.algorithm;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * @auther xianzhan
 * @sinese 2018-04-07
 */
public class SortTest {

    private int[] arr;

    @Before
    public void before() {
        arr = new Random().ints(10, 1, 100).toArray();
    }

    @Test
    public void bubble() {
        printArr(arr);
        Sort.bubble(arr);
        printArr(arr);
    }

    @Test
    public void cocktail() {
        printArr(arr);
        Sort.cocktail(arr);
        printArr(arr);
    }

    @Test
    public void insertion() {
        printArr(arr);
        Sort.insertion(arr);
        printArr(arr);
    }

    /**
     * 打印数组
     */
    private void printArr(int[] arr) {
        System.out.println(Arrays.toString(arr));
    }
}
