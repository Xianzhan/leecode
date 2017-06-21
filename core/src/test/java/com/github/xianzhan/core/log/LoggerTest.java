package com.github.xianzhan.core.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/6/21
 */
public class LoggerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void info() {
        LOGGER.info("info");
    }

    @Test
    public void warn() {
        LOGGER.warn("warn");
    }

    @Test
    public void debug() {
        LOGGER.debug("debug");
    }

    @Test
    public void error() {
        LOGGER.error("error");
    }
}
