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
 * 类操作工具类
 *
 * @author xianzhan
 * @since 2019-02-09
 */
public class Classes {

    /**
     * @return 当前线程加载类
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     *
     * @param className     全限定类名
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
            String fileName = packageName.replaceAll("\\.", "/");
            Enumeration<URL> resources = getClassLoader().getResources(fileName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    // .class
                    String packagePath = url.getPath().replace("%20", " ");
                    addClass(classSet, packagePath, packageName);
                } else if ("jar".equals(protocol)) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    Enumeration<JarEntry> jarEntries = jarFile.entries();

                    while (jarEntries.hasMoreElements()) {
                        JarEntry jarEntry = jarEntries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (!jarEntryName.endsWith(".class")) {
                            continue;
                        }

                        int lastIndexOf = jarEntryName.lastIndexOf('.');
                        String className = jarEntryName.substring(0, lastIndexOf).replaceAll("/", ".");
                        doAddClass(classSet, className);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File dir = new File(packagePath);
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles(file -> file.isFile() && file.getName().endsWith(".class"));
        if (Arrays.isEmpty(files)) {
            return;
        }

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }

            String fileName = file.getName();
            int lastIndexOf = fileName.lastIndexOf('.');
            String className = fileName.substring(0, lastIndexOf);
            if (Strings.isEmpty(className)) {
                String subPackagePath = fileName;
                if (!Strings.isEmpty(packageName)) {
                    subPackagePath = packagePath + '/' + subPackagePath;
                }
                String subPackageName = fileName;
                if (!Strings.isEmpty(packageName)) {
                    subPackageName = packageName + '.' + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            } else {
                className = packageName + '.' + className;
                doAddClass(classSet, className);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> loadClass = loadClass(className, false);
        classSet.add(loadClass);
    }
}
