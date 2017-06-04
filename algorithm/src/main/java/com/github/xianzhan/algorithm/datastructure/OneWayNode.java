package com.github.xianzhan.algorithm.datastructure;

/**
 * 描述：单向节点
 *
 * @author Lee
 * @since 2017/6/4
 */
public class OneWayNode<T> {
    public T value;
    public OneWayNode<T> next;

    public OneWayNode(T value) {
        this.value = value;
    }
}
