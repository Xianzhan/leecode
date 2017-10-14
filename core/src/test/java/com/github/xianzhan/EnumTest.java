package com.github.xianzhan;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/10
 */
public class EnumTest {
    public static void main(String[] args) {
        for (Week week : Week.values()) {
            System.out.println(week);
        }
    }
}

enum Week {
    MON, TUE, WED, THU, FRI, SAT, SUN;
}