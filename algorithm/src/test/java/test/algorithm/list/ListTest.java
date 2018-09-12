package test.algorithm.list;

import org.junit.Before;
import org.junit.Test;
import xianzhan.algorithm.list.List;

import java.util.function.IntFunction;

/**
 * @author xianzhan
 * @since 2018-09-12
 */
public class ListTest {

    private List list;
    private List.ListNode node;

    @Before
    public void before() {
        list = new List();

        IntFunction<List.ListNode> nodeNew = List.ListNode::new;
        node = nodeNew.apply(1);
        var two = nodeNew.apply(2);
        node.setNext(two);
        var three = nodeNew.apply(3);
        two.setNext(three);
        var four = nodeNew.apply(4);
        three.setNext(four);

        // 环
        four.setNext(two);
    }

    @Test
    public void testHasCycle() {
        System.out.println(list.hasCycle(node));
    }
}
