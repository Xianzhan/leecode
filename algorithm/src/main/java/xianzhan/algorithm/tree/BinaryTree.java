package xianzhan.algorithm.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树
 *
 * @author xianzhan
 * @since 2018-09-10
 */
public class BinaryTree {

    /**
     * 二叉树的最大深度
     *
     * @param node 二叉树节点
     * @return 最大深度
     */
    public int maxDeath(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int left = maxDeath(node.left);
        int right = maxDeath(node.right);
        return Math.max(left, right) + 1;
    }

    /**
     * 二叉树的最小深度
     *
     * @param node 二叉树节点
     * @return 最小深度
     */
    public int minDeath(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return getMinDeath(node);
    }

    private int getMinDeath(TreeNode node) {
        if (node == null) {
            return Integer.MAX_VALUE;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return Math.min(getMinDeath(node.left), getMinDeath(node.right)) + 1;
    }

    /**
     * 二叉树节点的个数
     *
     * @param node 二叉树节点
     * @return 节点的个数
     */
    public int numOfNode(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int left = numOfNode(node.left);
        int right = numOfNode(node.right);
        return left + right + 1;
    }

    /**
     * 二叉树没有子节点的个数
     *
     * @param node 二叉树节点
     * @return 左右节点都为 null 的个数
     */
    public int numOfNoChildNode(TreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return numOfNoChildNode(node.left) + numOfNoChildNode(node.right);
    }

    /**
     * 二叉树中第 level 层的节点个数
     *
     * @param node  二叉树节点
     * @param level 层数
     * @return 二叉树中第k层节点的个数
     */
    public int numOfLevel(TreeNode node, int level) {
        if (node == null || level < 1) {
            return 0;
        }
        if (level == 1) {
            return 1;
        }
        int numOfLeft = numOfLevel(node.left, level - 1);
        int numOfRight = numOfLevel(node.right, level - 1);
        return numOfLeft + numOfRight;
    }

    /**
     * 是否为平衡二叉树
     * <p>
     * https://zh.wikipedia.org/wiki/%E5%B9%B3%E8%A1%A1%E4%BA%8C%E5%85%83%E6%90%9C%E5%B0%8B%E6%A8%B9
     *
     * @param node 二叉树节点
     * @return true 为是
     */
    public boolean isBalanced(TreeNode node) {
        return balancedDeath(node) != -1;
    }

    private int balancedDeath(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int left = balancedDeath(node.left);
        int right = balancedDeath(node.right);
        if (left == -1 || right == -1 || Math.abs(left - right) > 1) {
            return -1;
        }
        return Math.max(left, right) + 1;
    }

    /**
     * 是否为完全二叉树
     *
     * @param node 二叉树节点
     * @return true 为是
     */
    public boolean isCompleteTree(TreeNode node) {
        if (node == null) {
            return false;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(node);
        boolean complete = true;
        boolean hasNoChild = false;
        while (!queue.isEmpty()) {
            TreeNode current = queue.remove();
            if (hasNoChild) {
                if (current.left != null || current.right != null) {
                    complete = false;
                    break;
                }
            } else {
                if (current.left != null && current.right != null) {
                    queue.add(current.left);
                    queue.add(current.right);
                } else if (current.left != null) {
                    queue.add(current.left);
                    hasNoChild = true;
                } else if (current.right != null) {
                    complete = false;
                    break;
                } else {
                    hasNoChild = true;
                }
            }
        }
        return complete;
    }

    /**
     * 两个二叉树是否完全相同
     *
     * @param node  二叉树节点
     * @param other 二叉树节点
     * @return true 为完全相同
     */
    public boolean isSame(TreeNode node, TreeNode other) {
        if (node == null && other == null) {
            return true;
        } else if (node == null || other == null) {
            return false;
        }
        if (node.value != other.value) {
            return false;
        }
        boolean left = isSame(node.left, other.left);
        boolean right = isSame(node.right, other.right);
        return left && right;
    }

    /**
     * 是否为镜像
     *
     * @param node  二叉树节点
     * @param other 二叉树节点
     * @return true 为镜像
     */
    public boolean isMirror(TreeNode node, TreeNode other) {
        if (node == null && other == null) {
            return true;
        }
        if (node == null || other == null) {
            return false;
        }
        if (node.value != other.value) {
            return false;
        }
        return isMirror(node.left, other.right) && isMirror(node.right, other.left);
    }

    public static class TreeNode {
        private int value;
        TreeNode left;
        TreeNode right;

        public TreeNode(int value) {
            this.value = value;
        }

        public TreeNode getLeft() {
            return left;
        }

        public void setLeft(TreeNode left) {
            this.left = left;
        }

        public TreeNode getRight() {
            return right;
        }

        public void setRight(TreeNode right) {
            this.right = right;
        }
    }
}
