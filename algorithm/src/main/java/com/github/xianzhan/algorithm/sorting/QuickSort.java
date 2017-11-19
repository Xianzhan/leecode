package com.github.xianzhan.algorithm.sorting;

import java.util.LinkedList;
import java.util.List;

/**
 * Uses partitioning to recursively divide and sort the list
 * <p>
 * Time Complexity: O(n**2) worst case
 * <p>
 * Space Complexity: O(n**2) this version
 * <p>
 * Stable: No
 * <p>
 * Psuedo Code: CLRS. Introduction to Algorithms. 3rd ed.
 */
public class QuickSort {

    static List<Integer> sort(List<Integer> seq) {
        // Takes a list of integers and sorts them in ascending order. This sorted
        // list is then returned.

        if (seq.size() <= 1) {
            return seq;
        }

        Integer pivot = seq.get(0);
        List<Integer> left = new LinkedList<>(), right = new LinkedList<>();
        for (Integer i : seq.subList(1, seq.size())) {
            if (i < pivot) {
                left.add(i);
            } else {
                right.add(i);
            }
        }
        List<Integer> all = new LinkedList<>();
        all.addAll(sort(left));
        all.add(pivot);
        all.addAll(sort(right));
        return all;
    }

    public static void main(String[] args) {
        System.out.println(sort(List.of(5, 3, 6, 2, 7, 1, 8, 9, 4, 44)));
    }
}
