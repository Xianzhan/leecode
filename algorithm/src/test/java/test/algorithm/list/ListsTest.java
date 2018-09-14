package test.algorithm.list;

import org.junit.Before;
import org.junit.Test;
import xianzhan.algorithm.list.Lists;

import java.util.function.IntFunction;

/**
 * @author xianzhan
 * @since 2018-09-12
 */
public class ListsTest {

    private Lists          lists;
    private Lists.ListNode node;

    @Before
    public void before() {
        lists = new Lists();

        IntFunction<Lists.ListNode> nodeNew = Lists.ListNode::new;
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
        System.out.println(lists.hasCycle(node));
    }
}
