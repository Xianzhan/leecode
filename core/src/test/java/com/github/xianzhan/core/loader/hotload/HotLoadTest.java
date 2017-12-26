package com.github.xianzhan.core.loader.hotload;

public class HotLoadTest {
    public static void main(String[] args) {
        new Thread(new HotLoaderHandler()).start();
    }
}
