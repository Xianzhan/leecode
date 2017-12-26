package com.github.xianzhan.core.loader.hotload.manager.impl;

import com.github.xianzhan.core.loader.hotload.manager.IManager;

/**
 * 实现 java 类的热加载功能
 */
public class ManagerImpl implements IManager {
    @Override
    public void logic() {
        System.out.println("你好, 我是热加载123");
        System.out.println(System.currentTimeMillis());
    }
}
