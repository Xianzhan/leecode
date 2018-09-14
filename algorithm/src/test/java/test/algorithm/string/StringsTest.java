package test.algorithm.string;

import org.junit.Before;
import org.junit.Test;
import xianzhan.algorithm.string.Strings;

/**
 * @author xianzhan
 * @since 2018-09-14
 */
public class StringsTest {

    private Strings strings;

    @Before
    public void before() {
        strings = new Strings();
    }

    @Test
    public void testRotateString() {
        System.out.println(strings.rotateString("hello world", 3));
    }
}
