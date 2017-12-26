package com.github.xianzhan.core.loader.hotload;

import java.io.*;

/**
 * 自定义 Java 类加载器,
 * 实现 Java 类的热加载
 */
public class HotClassLoader extends ClassLoader {

    /**
     * 要加载的 Java 类的 classpath 路径
     */
    private String classpath;

    public HotClassLoader(String classpath) {
        super(ClassLoader.getSystemClassLoader());
        this.classpath = classpath;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        if (data == null) {
            return null;
        }
        return this.defineClass(name, data, 0, data.length);
    }

    /**
     * 加载 class 文件中的数据
     *
     * @param name
     * @return
     */
    private byte[] loadClassData(String name) {
        try {
            name = name.replace(".", "//");
            File classFile = new File(classpath + name + ".class");

            if (classFile.exists()) {
                FileInputStream fis = new FileInputStream(classFile);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int b = 0;
                while ((b = fis.read()) != -1) {
                    baos.write(b);
                }
                fis.close();
                return baos.toByteArray();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
