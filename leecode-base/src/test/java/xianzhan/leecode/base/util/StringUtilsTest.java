package xianzhan.leecode.base.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 字符串工具测试类
 *
 * @author xianzhan
 * @see StringUtils
 * @since 2020-11-01
 */
public class StringUtilsTest {

    @Test
    public void testIsEmpty() {
        Assertions.assertTrue(StringUtils.isEmpty(null), "字符串不为 null");
        Assertions.assertTrue(StringUtils.isEmpty(StringUtils.EMPTY), "字符串不是空字符串");
        Assertions.assertFalse(StringUtils.isEmpty(" "), "字符串不是空白字符串");
        Assertions.assertFalse(StringUtils.isEmpty("a"), "字符串不是非空字符串");
    }

    @Test
    public void testLength() {
        Assertions.assertEquals(0, StringUtils.length(null), "长度不为 0");
        Assertions.assertEquals(0, StringUtils.length(StringUtils.EMPTY), "空字符串长度不为 0");
        Assertions.assertEquals(3, StringUtils.length("abc"), "abc 字符串长度不为 3");
    }
}
