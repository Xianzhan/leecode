package architecture.util;

/**
 * 字符串工具类
 * 
 * @author Lee
 *
 */
public class StringUtil {

	/**
	 * 字符串分隔符
	 */
	public static final String SEPARATOR = String.valueOf((char) 29);

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str) {
		if (str != null) {
			str = str.trim();
		}
		return org.apache.commons.lang3.StringUtils.isEmpty(str);
	}

	/**
	 * 判断字符串是否非空
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 根据 regex 分割字符串 body
	 * 
	 * @param body
	 * @param regex
	 * @return
	 */
	public static String[] splitString(String body, String regex) {

		return body.split(regex);
	}
}
