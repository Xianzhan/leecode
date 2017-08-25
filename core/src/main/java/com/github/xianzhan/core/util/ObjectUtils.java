package com.github.xianzhan.core.util;

import java.util.Objects;

/**
 * 描述：java 对象工具类
 *
 * @author Lee
 * @since 2017/8/26
 */
public class ObjectUtils {

    /**
     * 判断该对象是否为空
     *
     * @param obj 判断对象
     * @return
     * @see Objects#isNull(Object)
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 判断对象是否不为空
     *
     * @param obj
     * @return
     * @see Objects#nonNull(Object)
     */
    public static boolean nonNull(Object obj) {
        return obj != null;
    }

    /**
     * 将对象转为字符串返回
     *
     * @param obj  对象
     * @param init 对象为空时的默认字符串
     * @return 字符串
     */
    public static String toString(Object obj, String init) {
        return isNull(obj) ? init : obj.toString();
    }

    /**
     * 如果对象为空, 初始化对象
     *
     * @param obj  判断的对象
     * @param init 为空时返回的默认对象
     * @return 对象
     */
    public static Object ifNullInit(Object obj, Object init) {
        return nonNull(obj) ? obj : init;
    }
}
