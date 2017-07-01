package com.github.xianzhan.leetcode.a.a1;

import java.util.*;

/**
 * 描述：两个大数，用链表表示。您给做个加法。
 * eg.<br>
 *   list[5, 6, 7]
 * + list[5, 6, 7]
 * ---------------
 * list[1, 1, 3, 4]
 *
 * @author Lee
 * @since 2017/6/29
 */
public class TwoNumbers {

    public List<Integer> solution(List<Integer> a, List<Integer> b) {
        int base = 10; // 进制
        if (a.size() < b.size()) {
            List<Integer> tmp = a;
            a = b;
            b = tmp;
        }
        LinkedList<Integer> result = new LinkedList<>();
        int ap = a.size() - 1;
        int bp = b.size() - 1;
        boolean plusOne = false;
        int sum;

        for (; bp > 0; bp--, ap--) {
            sum = a.get(ap) + b.get(bp);
            if (sum >= 10) {
                a.set(ap - 1, a.get(ap - 1) + 1);
                sum %= base;
            }
            result.addFirst(sum);
        }

        // 最后一位拿出来相加
        sum = a.get(ap) + b.get(bp);
        if (sum >= 10) {
            sum %= base;
            plusOne = true;
        }
        result.addFirst(sum);

        // 将剩余的数插入结果头
        if (ap > 0) {
            if (plusOne) {
                a.set(ap - 1, a.get(ap) + 1);
            }
            while (ap > 0) {
                result.addFirst(a.get(--ap));
            }
        } else {
            result.addFirst(1);
        }

        return result;
    }

    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        Collections.addAll(a, 6, 2, 4);
        List<Integer> b = new ArrayList<>();
        Collections.addAll(b, 4, 5, 6);

        List<Integer> sum = new TwoNumbers().solution(a, b);
        Iterator<Integer> iterator = sum.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next());
        }
    }
}
