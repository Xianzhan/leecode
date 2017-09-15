package com.github.xianzhan.core.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * 描述：时间工具类
 *
 * @author Lee
 * @since 2017/9/15
 */
public class TimeUtils {

    private TimeUtils() {}

    /**
     * 每分钟拥有时秒数
     */
    public static final int SECONDS_PER_MINUTE = 60;
    /**
     * 每小时拥有分钟数
     */
    public static final int MINUTES_PER_HOUR   = 60;
    /**
     * 每天拥有小时数
     */
    public static final int HOURS_PER_DAY      = 24;
    /**
     * 每小时拥有时秒数
     */
    public static final int SECONDS_PER_HOUR   = SECONDS_PER_MINUTE *
                                                 MINUTES_PER_HOUR;
    /**
     * 每天拥有时秒数
     */
    public static final int SECONDS_PER_DAY    = SECONDS_PER_MINUTE *
                                                 MINUTES_PER_HOUR * HOURS_PER_DAY;

    /**
     * 获取当前年份
     *
     * @return 年
     */
    public static int getYear() {
        int year;
        if (isJava8()) {
            LocalDate localDate = LocalDate.now();
            year = localDate.getYear();
        } else {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        return year;
    }

    /**
     * 获取当前月数
     *
     * @return 月
     */
    public static int getMonth() {
        int month;
        if (isJava8()) {
            LocalDate localDate = LocalDate.now();
            month = localDate.getMonthValue();
        } else {
            Calendar calendar = Calendar.getInstance();
            month = calendar.get(Calendar.MONTH);
        }
        return month;
    }

    /**
     * 获取今天是这个月的第几天
     *
     * @return 天
     */
    public static int getDay() {
        int day;
        if (isJava8()) {
            LocalDate localDate = LocalDate.now();
            day = localDate.getDayOfMonth();
        } else {
            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        return day;
    }

    /**
     * 获取当天剩余多少秒
     *
     * @return 秒
     */
    public static int getSecondsRemainingOfDay() {
        int seconds;
        if (isJava8()) {
            LocalTime localTime = LocalTime.now();
            int hour = localTime.getHour();
            int minute = localTime.getMinute();
            int second = localTime.getSecond();
            seconds = hour * SECONDS_PER_HOUR + minute * SECONDS_PER_MINUTE +
                      second;
        } else {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            seconds = hour * SECONDS_PER_HOUR + minute * SECONDS_PER_MINUTE +
                      second;
        }
        return SECONDS_PER_DAY - seconds;
    }

    private static boolean isJava8() {
        return SystemUtils.getJavaVersion().startsWith("1.8");
    }

    public static void main(String[] args) {
        System.out.println(getYear());
        System.out.println(getMonth());
        System.out.println(getDay());
        System.out.println(getSecondsRemainingOfDay());
    }
}
