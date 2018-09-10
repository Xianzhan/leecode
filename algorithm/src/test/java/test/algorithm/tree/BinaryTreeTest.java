package test.algorithm.tree;

import org.junit.Before;
import org.junit.Test;
import xianzhan.algorithm.tree.BinaryTree;

import java.util.function.IntFunction;

/**
 * @author xianzhan
 * @since 2018-09-10
 */
public class BinaryTreeTest {

    private BinaryTree          tree;
    private BinaryTree.TreeNode node;

    @Before
    public void before() {
        tree = new BinaryTree();

        IntFunction<BinaryTree.TreeNode> nodeNew = BinaryTree.TreeNode::new;
        node = nodeNew.apply(1);
        node.setLeft(nodeNew.apply(2));
        node.setRight(nodeNew.apply(3));
        var two = node.getLeft();
        two.setRight(nodeNew.apply(5));
    }

    @Test
    public void testMaxDeath() {
        System.out.println(tree.maxDeath(node));
    }

    @Test
    public void testMinDeath() {
        System.out.println(tree.minDeath(node));
    }

    @Test
    public void testNumOfNode() {
        System.out.println(tree.numOfNode(node));
    }

    @Test
    public void testNumOfNoChildNode() {
        System.out.println(tree.numOfNoChildNode(node));
    }

    @Test
    public void testNumOfLevel() {
        System.out.println(tree.numOfLevel(node, 3));
    }

    @Test
    public void testIsBalanced() {
        System.out.println(tree.isBalanced(node));
    }

    @Test
    public void testIsCompleteTree() {
        System.out.println(tree.isCompleteTree(node));
    }

    @Test
    public void testIsSame() {
        System.out.println(tree.isSame(node, node));
    }

    @Test
    public void testIsMirror() {
        System.out.println(tree.isMirror(node, node));
    }
}
