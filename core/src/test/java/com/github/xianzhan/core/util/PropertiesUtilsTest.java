package com.github.xianzhan.core.util;

import org.junit.Test;

import java.util.Properties;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/6/22
 */
public class PropertiesUtilsTest {

    @Test
    public void getProperties() {
        Properties properties = PropertiesUtils.getProperties
                ("log4j.properties");
        System.out.println(properties.getProperty("log4j.rootCategory"));
    }
}
