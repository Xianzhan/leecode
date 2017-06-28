package com.github.xianzhan.leetcode.a.a1;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：给定一个整数数组和一个值target，
 * 求两个下标i、j，使得a[i] + a[j] = target，
 * 返回下标。
 *
 * @author Lee
 * @since 2017/6/28
 */
public class TwoSum {

    public int[] solution(int[] arr, int target) {
        Map<Integer, Integer> map = new HashMap<>(arr.length);
        int[] result = new int[2];
        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(target - arr[i])) {
                result[0] = map.get(target - arr[i]);
                result[1] = i;
                break;
            } else {
                map.put(arr[i], i);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr = {1, 5, 9, 11, 66, 7, 16};
        int target = 77;
        int[] result = new TwoSum().solution(arr, target);
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
    }
}
