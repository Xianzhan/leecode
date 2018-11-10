package test.core.util;

import org.junit.Test;
import xianzhan.core.util.Strings;

/**
 * @author xianzhan
 * @since 2018-11-11
 */
public class StringsTest {

    @Test
    public void testDouble() {
        System.out.println(Double.valueOf("+1"));
        System.out.println(Strings.isDouble("+1"));

        System.out.println(Double.valueOf("+1."));
        System.out.println(Strings.isDouble("+1."));

        System.out.println(Double.valueOf("+.1"));
        System.out.println(Strings.isDouble("+.1"));

        System.out.println(Double.valueOf(".1"));
        System.out.println(Strings.isDouble(".1"));

        System.out.println(Double.valueOf("-.1"));
        System.out.println(Strings.isDouble("-.1"));

        System.out.println(Double.valueOf("1"));
        System.out.println(Strings.isDouble("1"));

        System.out.println(Double.valueOf("111111111111111"));
        System.out.println(Strings.isDouble("111111111111111"));

        System.out.println(Strings.isDouble("++"));
        System.out.println(Strings.isDouble("ad"));
    }
}
