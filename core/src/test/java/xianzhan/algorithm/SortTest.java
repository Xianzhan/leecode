package xianzhan.algorithm;

import org.junit.Test;

import java.util.Arrays;

/**
 * @auther xianzhan
 * @sinese 2018-04-07
 */
public class SortTest {

    @Test
    public void bubble() {
        int[] arr = Sort.arr();
        System.out.println(Arrays.toString(arr));
        Sort.bubble(arr);
        System.out.println(Arrays.toString(arr));
    }
}
