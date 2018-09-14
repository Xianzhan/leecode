package xianzhan.algorithm.list;

/**
 * 链表
 *
 * @author xianzhan
 * @since 2018-09-12
 */
public class Lists {

    /**
     * 判断链表是否存在环
     *
     * @param head 链表
     * @return true 为存在
     */
    public boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        ListNode node1 = head, node2 = head.next;
        while (node1 != null && node2 != null && node2.next != null) {
            if (node1 == node2) {
                return true;
            }
            node1 = node1.next;
            node2 = node2.next.next;
        }
        return false;
    }

    public static class ListNode {
        int      value;
        ListNode next;

        public ListNode(int value) {
            this.value = value;
            this.next = null;
        }

        public ListNode getNext() {
            return next;
        }

        public void setNext(ListNode next) {
            this.next = next;
        }
    }
}
