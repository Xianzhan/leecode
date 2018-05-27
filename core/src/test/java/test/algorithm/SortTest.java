package test.algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xianzhan.algorithm.Sort;

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
        arr = new Random().ints(11, 1, 100).toArray();
        printArr(arr);
    }

    @After
    public void after() {
        printArr(arr);
    }

    /**
     * 打印数组
     */
    private void printArr(int[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void bubble() {
        Sort.bubble(arr);
    }

    @Test
    public void cocktail() {
        Sort.cocktail(arr);
    }

    @Test
    public void insertion() {
        Sort.insertion(arr);
    }

    @Test
    public void merge() {
        Sort.merge(arr);
    }

    @Test
    public void shell() {
        Sort.shell(arr);
    }

    @Test
    public void selection() {
        Sort.selection(arr);
    }

    @Test
    public void heap() {
        Sort.heap(arr);
    }

    @Test
    public void quick() {
        Sort.quick(arr);
    }
}
