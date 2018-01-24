package com.github.xianzhan.practice.leetcode.easy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Given an array of integers, return indices of the two numbers such that they add up to a specific target.<br>
 * You may assume that each input would have exactly one solution, and you may not use the same element twice.<br>
 * Example:
 * <pre>
 *     Given nums = [2, 7, 11, 15], target = 9,
 *     Because nums[0] + nums[1] = 2 + 7 = 9,
 *     return [0, 1].
 * </pre>
 */
public class TwoSum {

    private static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return nums;
    }

    public static void main(String[] args) {
        int[] nums = {1, 4, 6, 8, 9, 13};
        int target = 12;
        int[] result = twoSum(nums, target);
        System.out.println(Arrays.toString(result));
    }
}
