package com.github.xianzhan.leetcode.a.a1;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：给定一个字符串, 找出字母不带重样的最长子串
 *
 * @author Lee
 * @since 2017/7/2
 */
public class LongestSubstringWithoutRepeatingCharacters {

    public int solution(String s) {
        if (s == null || s.length() == 0) return 0;
        Map<Character, Integer> map = new HashMap<>();
        int max = 0;
        // i 遍历 s
        // j 当前最大子串起始位置
        for (int i = 0, j = 0; i < s.length(); i++) {
            if (map.containsKey(s.charAt(i))) {
                j = Math.max(j, map.get(s.charAt(i)) + 1);
            }
            map.put(s.charAt(i), i);
            max = Math.max(max, i - j + 1);
        }
        return max;
    }

    public static void main(String[] args) {
        String s = "sldafkjlsjflasjfkljlkldksja";
        LongestSubstringWithoutRepeatingCharacters l = new
                LongestSubstringWithoutRepeatingCharacters();
        System.out.println(l.solution(s));
    }
}
