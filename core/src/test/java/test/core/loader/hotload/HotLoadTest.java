package test.core.loader.hotload;

import xianzhan.core.loader.hotload.HotLoaderHandler;

public class HotLoadTest {
    public static void main(String[] args) {
        new Thread(new HotLoaderHandler()).start();
    }
}
