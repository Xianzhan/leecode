package xianzhan.leecode.base.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author xianzhan
 * @since 2020-11-19
 */
public class DateUtilsTest {

    private final Date start = new Date(0L);

    @BeforeEach
    public void before() {
        System.setProperty("user.timezone", "Asia/Shanghai");
    }

    @Test
    public void testToLocalTime() {
        var localTime = DateUtils.toLocalTime(start);

        Assertions.assertEquals(8, localTime.getHour());
        Assertions.assertEquals(0, localTime.getMinute());
        Assertions.assertEquals(0, localTime.getSecond());
    }
}
