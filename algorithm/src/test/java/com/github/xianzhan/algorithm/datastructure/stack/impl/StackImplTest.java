package com.github.xianzhan.algorithm.datastructure.stack.impl;

import com.github.xianzhan.algorithm.datastructure.stack.Stack;
import org.junit.Test;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/6/4
 */
public class StackImplTest {

    @Test
    public void push() {
        Stack<Integer> stack = new StackImpl();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.size());
    }

    @Test
    public void pop() {
        Stack<Integer> stack = new StackImpl();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.pop());
        System.out.println(stack.size());
    }
}
