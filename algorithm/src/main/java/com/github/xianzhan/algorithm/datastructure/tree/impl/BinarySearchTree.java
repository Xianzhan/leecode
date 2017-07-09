package com.github.xianzhan.algorithm.datastructure.tree.impl;

/**
 * 描述：二叉查找树
 *
 * @author Lee
 * @since 2017/7/9
 */
public class BinarySearchTree<T extends Comparable<? super T>> {
    private static class BinaryNode<T> {
        BinaryNode(T theElement) {
            this(theElement, null, null);
        }

        BinaryNode(T theElement, BinaryNode<T> lt, BinaryNode<T> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        T element;
        BinaryNode<T> left; // Left child
        BinaryNode<T> right; // Right child
    }

    private BinaryNode<T> root;

    public BinarySearchTree() {
        makeEmpty();
    }

    public void makeEmpty() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(T x) {
        return contains(x, root);
    }

    public T findMin() {
        if (isEmpty()) throw new NullPointerException();
        return findMin(root).element;
    }

    public T findMax() {
        if (isEmpty()) throw new NullPointerException();
        return findMax(root).element;
    }

    private BinaryNode<T> findMax(BinaryNode<T> t) {
        if (t != null)
            while (t.right != null)
                t = t.right;
        return t;
    }

    public void insert(T x) {
        root = insert(x, root);
    }

    public void remove(T x) {
        root = remove(x, root);
    }

    public void printTree() {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root);
    }

    private void printTree(BinaryNode<T> t) {
        if (t != null) {
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

    private BinaryNode<T> remove(T x, BinaryNode<T> t) {
        if (t == null) return t; // Item not found; do nothing

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            t.left = remove(x, t.left);
        else if (compareResult > 0)
            t.right = remove(x, t.right);
        else if (t.left != null && t.right != null) {
            t.element = findMin(t.right).element;
            t.right = remove(t.element, t.right);
        } else
            t = (t.left != null) ? t.left : t.right;

        return t;
    }

    private BinaryNode<T> insert(T x, BinaryNode<T> t) {
        if (t == null) return new BinaryNode<T>(x, null, null);

        int compareResult = x.compareTo(t.element);
        if (compareResult < 0) t.left = insert(x, t.left);
        else if (compareResult > 0) t.right = insert(x, t.right);
        else ;

        return t;
    }

    private BinaryNode<T> findMin(BinaryNode<T> t) {
        if (t == null) return null;
        else if (t.left == null) return t;
        return findMin(t.left);
    }

    private boolean contains(T x, BinaryNode<T> t) {
        if (t == null) return false;
        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return contains(x, t.left);
        else if (compareResult > 0)
            return contains(x, t.right);
        else
            return true;
    }
}
