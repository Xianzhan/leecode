package com.github.xianzhan.algorithm.datastructure.stack;

/**
 * 描述：数据结构——栈
 *
 * @author Lee
 * @since 2017/6/4
 */
public interface Stack<T> {

    /**
     * 变为空栈
     */
    void clear();

    /**
     * item 入栈，成功返回真，否则假
     * @param item
     * @return
     */
    boolean push(T item);

    /**
     * 出栈
     * @return
     */
    T pop();

    /**
     * 读栈顶但不弹出
     * @return
     */
    T top();

    /**
     * 若栈已空返回真
     * @return
     */
    boolean isEmpty();

    /**
     * 返回栈大小
     * @return
     */
    int size();
}
