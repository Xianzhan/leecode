package xianzhan.core.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述：线程工具类
 * <p>
 * 该工具类用于一定时间内完成的任务
 *
 * @author xianzhan
 * @since 2017-10-07
 */
public final class ThreadUtils {

    private ThreadUtils() {
        throw new AssertionError("Thread 工具类不可实例化");
    }

    private static final int                     NUMBER_OF_WORKERS = Runtime.getRuntime().availableProcessors() << 1;
    private static       int                     maximumPoolSize   = NUMBER_OF_WORKERS << 1;
    private static       long                    keepAliveTime     = 120L;
    private static       BlockingQueue<Runnable> workQueue         = new LinkedBlockingQueue<>(1024);

    private static ExecutorService THREAD_POOL = new ThreadPoolExecutor(
            // 默认为 CPU 的两倍
            NUMBER_OF_WORKERS,
            // 该线程池的最大数量
            maximumPoolSize,
            // 当线程数大于核心线程数时, 空闲时等待的时间
            keepAliveTime,
            // keepAliveTime 的单位
            TimeUnit.SECONDS,
            // 当数量超过最大线程池数量时时保存到这里
            workQueue,
            // 创建线程时设置一些配置
            new LeeThreadFactory(),
            // 超过 workQueue 设置数量时的回调函数
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 执行任务
     *
     * @param runnable 任务
     */
    public static void execute(Runnable runnable) {
        THREAD_POOL.execute(runnable);
    }

    /**
     * 关闭线程
     */
    public static void shutdown() {
        THREAD_POOL.shutdown();
    }

    private static class LeeThreadFactory implements ThreadFactory {
        // 创建线程时的自定义参数

        private static final AtomicInteger POOL_NUMBER  = new AtomicInteger(1);
        private final        AtomicInteger threadNumber = new AtomicInteger(1);
        private final        String        namePrefix;
        private final        ThreadGroup   group;

        private LeeThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "Xianzhan-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
