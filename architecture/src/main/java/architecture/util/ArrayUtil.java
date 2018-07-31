package architecture.util;

import org.apache.commons.lang3.ArrayUtils;

public class ArrayUtil {

	/**
	 * 判断数组是否非空
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNotEmpty(Object[] array) {
		return !ArrayUtils.isEmpty(array);
	}

	/**
	 * 判断数组是否为空
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		return ArrayUtils.isEmpty(array);
	}
}
