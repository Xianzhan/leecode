package xianzhan.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import xianzhan.service.IAsyncService;

import java.util.concurrent.Future;

/**
 * @author xianzhan
 * @since 2018-05-15
 */
@Service
public class AsyncServiceImpl implements IAsyncService {

    @Async
    @Override
    public Future<Integer> sleep5seconds() {
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(5);
    }

    @Override
    public Future<Integer> sleep7seconds() {
        try {
            Thread.sleep(7_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(7);
    }
}
