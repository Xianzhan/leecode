package com.github.xianzhan.core.loader.hotload;

import com.github.xianzhan.core.loader.hotload.manager.IManager;
import com.github.xianzhan.core.loader.hotload.manager.factory.ManagerFactory;

public class HotLoaderHandler implements Runnable {
    @Override
    public void run() {

        try {
            while (true) {
                IManager manager = ManagerFactory.getManager(ManagerFactory.MANAGER_CLASS);
                manager.logic();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
