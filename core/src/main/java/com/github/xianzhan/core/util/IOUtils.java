package com.github.xianzhan.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/8/17
 */
public class IOUtils {

    /**
     * 不推荐使用此方法
     *
     * @param src    源文件路径
     * @param target 目标文件路径
     * @throws IOException
     */
    public static void copyBytes(String src, String target) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(target);
            int c;

            // 此处读磁盘, 多少字节读多少次, 效率底下
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }
}
