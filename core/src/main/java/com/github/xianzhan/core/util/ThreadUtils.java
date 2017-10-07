package com.github.xianzhan.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/7
 */
public class ThreadUtils {

    private ThreadUtils() {}

    private static final int THREAD_SIZE = Runtime.getRuntime()
            .availableProcessors();

    private static ExecutorService THREAD_POOL = Executors.newFixedThreadPool
            (THREAD_SIZE);

    public static void execute(Runnable runnable) {
        THREAD_POOL.execute(runnable);
    }

    public static void close() {
        THREAD_POOL.shutdown();
    }
}
