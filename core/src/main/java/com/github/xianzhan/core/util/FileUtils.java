package com.github.xianzhan.core.util;

import com.github.xianzhan.core.util.function.file.FileRead;

import java.io.*;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/7
 */
public class FileUtils {

    public static void readFile(String path, FileRead callback) {
        ThreadUtils.execute(() -> {
            Exception err = null;
            File file = null;
            try {
                file = new File(path);
            } catch (Exception e) {
                err = e;
            }
            callback.read(err, file);
        });

    }

    public static void main(String[] args) {
        FileUtils.readFile("./", (err, data) -> {
            if (ObjectUtils.nonNull(err)) {
                System.err.println(err);
            } else {
                String[] files = data.list();
                for (String file : files) {
                    System.out.println(file);
                }
            }
        });
        System.out.println("main");
    }
}
