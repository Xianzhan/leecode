package xianzhan.core.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class 加载器
 *
 * @author xianzhan
 * @since 2018-06-21
 */
@SuppressWarnings("unused")
public class ClassUtils {

    /**
     * 获取类加载
     *
     * @return 当前线程上下文加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     *
     * @param className     全路径名
     * @param isInitialized 是否初始化
     * @return Class
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }

    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<>();
        try {
            String resourcesName = packageName.replace(".", "/");
            Enumeration<URL> resources = getClassLoader().getResources(resourcesName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String packagePath = url.getPath().replace("%20", " ");
                    addClass(classSet, packagePath, packageName);
                } else if ("jar".equals(protocol)) {
                    JarURLConnection jarConn = (JarURLConnection) url.openConnection();
                    JarFile jarFile = jarConn.getJarFile();
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.endsWith(".class")) {
                            int lastIndexOf = jarEntryName.lastIndexOf(".");
                            String fileName = jarEntryName.substring(0, lastIndexOf);
                            String className = fileName.replace("/", ".");
                            doAddClass(classSet, className);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(file ->
                file.isDirectory() || (file.isFile() && file.getName().endsWith(".class"))
        );
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                // .class
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (packageName != null) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                // 子包
                String subPackagePath = packagePath + "/" + fileName;
                String subPackageName = packageName + "." + fileName;
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> aClass = loadClass(className, false);
        classSet.add(aClass);
    }
}
