package xianzhan.core.loader.hotload.manager.impl;


import xianzhan.core.loader.hotload.manager.IManager;

/**
 * 实现 java 类的热加载功能
 */
public class ManagerImpl implements IManager {
    @Override
    public void logic() {
        System.out.println("你好, 热加载~");
        System.out.println(System.currentTimeMillis());
    }
}
