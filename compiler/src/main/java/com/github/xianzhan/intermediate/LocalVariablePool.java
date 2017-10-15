package com.github.xianzhan.intermediate;

import java.util.LinkedList;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public class LocalVariablePool {
    private int                 nextIndex = 0;
    private LinkedList<Integer> indexPool = new LinkedList<>();

    int createIndex() {
        int index;
        if (indexPool.isEmpty()) {
            index = nextIndex;
            nextIndex++;
        } else {
            index = indexPool.removeFirst();
        }
        return index;
    }

    void freeIndex(int index) {
        if (index != -1) {
            indexPool.addLast(index);
        }
    }
}
