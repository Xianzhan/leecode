package com.github.xianzhan.algorithm.datastructure.list.impl;

import com.github.xianzhan.algorithm.datastructure.list.List;
import org.junit.Test;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/6/8
 */
public class ArrListTest {

    private List<Integer> list = new ArrList<>(3);

    @Test
    public void append() {
        for (int i = 0; i < 4; i++) {
            list.append(i);
        }
        System.out.println(list);
    }

    @Test
    public void insert() {
        for (int i = 0; i < 4; i++) {
            list.insert(1, i);
        }
        System.out.println(list);
    }

    @Test
    public void getPos() {
        for (int i = 0; i < 3; i++) {
            list.append(i + 4);
        }
        System.out.println(list.getPos(5));
    }
}
