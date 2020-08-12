package xianzhan.misc.jdk5.proxy;

import java.util.concurrent.TimeUnit;

/**
 * @author xianzhan
 * @since 2020-08-12
 */
public class Man implements Human {
    @Override
    public void run() {
        System.out.println("Man run");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
