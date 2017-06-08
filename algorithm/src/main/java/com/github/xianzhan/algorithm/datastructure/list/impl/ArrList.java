package com.github.xianzhan.algorithm.datastructure.list.impl;

import com.github.xianzhan.algorithm.datastructure.list.List;

import java.util.Arrays;

/**
 * 描述：顺序表
 *
 * @author Lee
 * @since 2017/6/6
 */
public class ArrList<T> implements List {

    private Object[] arr;
    private int size;

    public ArrList() {
        arr = new Object[10];
        size = 0;
    }

    public ArrList(int capacity) {
        arr = new Object[capacity];
        size = 0;
    }

    @Override
    public void clear() {
        for (; size > 0; size--) {
            arr[size] = null;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean append(Object value) {
        if (isFull()) {
            System.err.println("顺序表已满");
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                arr[i] = value;
                size++;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean insert(int p, Object value) {
        if (isFull()) {
            System.err.println("顺序表已满");
            return false;
        }
        if (arr[p] == null) {
            size++;
        }
        arr[p] = value;
        return true;
    }

    @Override
    public boolean delete(int p) {
        if (p < 0 || arr.length <= p) {
            System.err.println("范围错误");
            return false;
        }
        if (arr[p] == null) {
            return false;
        }
        arr[p] = null;
        return true;
    }

    @Override
    public int getPos(Object value) {
        if (value == null) {
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            if (value.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object getValue(int p) {
        if (p < 0 || arr.length <= p) {
            System.err.println("范围错误");
        }
        return arr[p];
    }

    @Override
    public boolean setValue(int p, Object value) {
        if (0 < p || arr.length <= p) {
            System.err.println("范围错误");
            return false;
        }
        arr[p] = value;
        return true;
    }

    private boolean isFull() {
        return size == arr.length;
    }

    @Override
    public String toString() {
        return "ArrList{" +
                "arr=" + Arrays.toString(arr) +
                ", size=" + size +
                '}';
    }
}
