package com.github.xianzhan.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 描述：Properties 文件工具类
 *
 * @author Lee
 * @since 2017/6/22
 */
public class PropertiesUtils {

    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        InputStream is = PropertiesUtils.class.getClassLoader()
                .getResourceAsStream(fileName);
        try {
            properties.load(is);
        } catch (IOException e) {

        }
        return properties;
    }
}
