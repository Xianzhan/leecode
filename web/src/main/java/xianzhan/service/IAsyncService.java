package xianzhan.service;

import java.util.concurrent.Future;

/**
 * @author xianzhan
 * @since 2018-05-15
 */
public interface IAsyncService {

    /**
     * 当前线程 sleep 5 秒
     *
     * @return 5
     */
    Future<Integer> sleep5seconds();

    /**
     * 当前线程 sleep 7 秒
     *
     * @return 7
     */
    Future<Integer> sleep7seconds();
}
