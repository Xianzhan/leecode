package test.core.util;

import org.junit.Test;
import xianzhan.core.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xianzhan
 * @since 2018-11-11
 */
public class StringsTest {

    @Test
    public void testDouble() {
        System.out.println(Double.valueOf("+1"));
        System.out.println(StringUtils.isDouble("+1"));

        System.out.println(Double.valueOf("+1."));
        System.out.println(StringUtils.isDouble("+1."));

        System.out.println(Double.valueOf("+.1"));
        System.out.println(StringUtils.isDouble("+.1"));

        System.out.println(Double.valueOf(".1"));
        System.out.println(StringUtils.isDouble(".1"));

        System.out.println(Double.valueOf("-.1"));
        System.out.println(StringUtils.isDouble("-.1"));

        System.out.println(Double.valueOf("1"));
        System.out.println(StringUtils.isDouble("1"));

        System.out.println(Double.valueOf("111111111111111"));
        System.out.println(StringUtils.isDouble("111111111111111"));

        System.out.println(StringUtils.isDouble("++"));
        System.out.println(StringUtils.isDouble("ad"));
    }

    public static void main(String[] args) {
        List<String> collect = Stream.of("hello", "world")
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        collect.forEach(System.out::println);
    }
}
