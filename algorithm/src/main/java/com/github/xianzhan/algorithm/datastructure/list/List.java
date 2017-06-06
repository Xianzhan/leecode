package com.github.xianzhan.algorithm.datastructure.list;

/**
 * 描述：线性表
 *
 * @author Lee
 * @since 2017/6/5
 */
public interface List<T> {

    /**
     * 置空线性表
     */
    void clear();

    /**
     * 线性表为空时，返回 true
     * @return
     */
    boolean isEmpty();

    /**
     * 在表尾添加一个元素
     * @param value
     * @return
     */
    boolean append(T value);

    /**
     * 在位置 p 上插入一个元素 value
     * @param p
     * @param value
     * @return
     */
    boolean insert(int p, T value);

    /**
     * 删除位置 p 上的元素
     * @param p
     * @return
     */
    boolean delete(int p);

    /**
     * 查找值为 value 的元素并返回其位置
     * @param value
     * @return
     */
    int getPos(T value);

    /**
     * 把位置 p 元素返回
     * @param p
     * @return
     */
    T getValue(int p);

    /**
     * 用 value 修改位置 p 的元素值
     * @param p
     * @param value
     * @return
     */
    boolean setValue(int p, T value);
}
