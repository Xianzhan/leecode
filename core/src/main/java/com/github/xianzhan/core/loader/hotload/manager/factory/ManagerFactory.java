package com.github.xianzhan.core.loader.hotload.manager.factory;

import com.github.xianzhan.core.loader.hotload.HotClassLoader;
import com.github.xianzhan.core.loader.hotload.HotLoaderInfo;
import com.github.xianzhan.core.loader.hotload.manager.IManager;
import com.github.xianzhan.core.loader.hotload.manager.impl.ManagerImpl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 加载 manager 的工厂
 */
public class ManagerFactory {

    /**
     * 记录热加载类的加载信息
     */
    private static final Map<String, HotLoaderInfo> LOAD_INFO_MAP = new HashMap<>();
    /**
     * maven 项目路径 + class 文件路径
     */
    private static final String CLASS_PATH = Paths.get(".") + "/core/target/classes/";
    /**
     * 实现热加载类的全名称(包名 + 类名)
     */
    public static final String MANAGER_CLASS = "com.github.xianzhan.core.loader.hotload.manager.impl.ManagerImpl";

    public static IManager getManager(String className) {
        File loadFile = new File(CLASS_PATH + className.replaceAll("\\.", "/") + ".class");
        long lastModified = loadFile.lastModified();

        if (LOAD_INFO_MAP.get(className) == null) {
            load(className, lastModified);
        } else if (LOAD_INFO_MAP.get(className).getLoadTime() != lastModified) {
            load(className, lastModified);
        }
        return LOAD_INFO_MAP.get(className).getManager();
    }

    private static void load(String className, long lastModified) {
        HotClassLoader hotClassLoader = new HotClassLoader(CLASS_PATH);
        Class<?> loadClass;
        try {
            loadClass = hotClassLoader.findClass(className);
            if (loadClass != null) {
                IManager manager = newInstance(loadClass);
                HotLoaderInfo loadInfo = new HotLoaderInfo(hotClassLoader, lastModified);
                loadInfo.setManager(manager);
                LOAD_INFO_MAP.put(className, loadInfo);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static IManager newInstance(Class<?> loadClass) {
        IManager manager = null;

        try {
            if (loadClass.getName().equals(ManagerImpl.class.getName())) {
                Constructor<?> constructor = loadClass.getConstructor();
                manager = (IManager) constructor.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return manager;
    }
}