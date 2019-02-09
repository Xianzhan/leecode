package xianzhan.core.util;

/**
 * 数组工具类
 *
 * @author xianzhan
 * @since 2019-02-09
 */
public class Arrays {

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @return true 数组为空
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}
