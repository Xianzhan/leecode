package xianzhan.leecode.base.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author xianzhan
 * @since 2020-11-17
 */
public class DateUtils {

    /**
     * 返回 date 的 LocalTime 类型
     *
     * @param date Date
     * @return date 的 LocalTime 类型
     */
    public static LocalTime toLocalTime(Date date) {
        if (date == null) {
            return null;
        }

        var instant = date.toInstant();
        var zoneId = ZoneId.systemDefault();
        return LocalTime.ofInstant(instant, zoneId);
    }

    /**
     * 返回 date 的 LocalDate 类型
     *
     * @param date Date
     * @return date 的 LocalDate 类型
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        var instant = date.toInstant();
        var zoneId = ZoneId.systemDefault();
        return LocalDate.ofInstant(instant, zoneId);
    }

    /**
     * 返回 date 的 LocalDateTime 类型
     *
     * @param date Date
     * @return date 的 LocalDateTime 类型
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }

        var instant = date.toInstant();
        var zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}
