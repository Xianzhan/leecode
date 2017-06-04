package com.github.xianzhan.algorithm.datastructure.stack.impl;

import com.github.xianzhan.algorithm.datastructure.OneWayNode;
import com.github.xianzhan.algorithm.datastructure.stack.Stack;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/6/4
 */
public class StackImpl<T> implements Stack {

    private OneWayNode node;
    private int size;

    @Override
    public void clear() {
        node = null;
        size = 0;
    }

    @Override
    public boolean push(Object item) {
        if (node == null) {
            node = new OneWayNode<>(item);
        } else {
            OneWayNode tmp = new OneWayNode(item);
            tmp.next = node;
            node = tmp;
        }
        size++;
        return true;
    }

    @Override
    public Object pop() {
        if (node == null) {
            throw new NullPointerException("栈未拥有任何值");
        }
        OneWayNode tmp = node;
        node = tmp.next;
        size--;
        return tmp.value;
    }

    @Override
    public Object top() {
        if (node == null) {
            throw new NullPointerException("栈未拥有任何值");
        }
        return node.value;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }
}
