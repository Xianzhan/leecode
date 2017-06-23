package com.github.xianzhan.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger
            (PropertiesUtils.class);

    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        InputStream is = PropertiesUtils.class.getClassLoader()
                .getResourceAsStream(fileName);
        try {
            properties.load(is);
        } catch (IOException e) {
            LOGGER.error("[PropertiesUtils][getProperties]出现IOException异常:" + e);
        }
        return properties;
    }
}
