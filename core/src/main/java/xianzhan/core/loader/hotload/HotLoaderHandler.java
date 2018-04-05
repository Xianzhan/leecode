package xianzhan.core.loader.hotload;

import xianzhan.core.loader.hotload.manager.IManager;
import xianzhan.core.loader.hotload.manager.factory.ManagerFactory;

import java.util.concurrent.TimeUnit;

public class HotLoaderHandler implements Runnable {
    @Override
    public void run() {

        try {
            while (true) {
                IManager manager = ManagerFactory.getManager(ManagerFactory.MANAGER_CLASS);
                manager.logic();
                TimeUnit tu = TimeUnit.SECONDS;
                tu.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
