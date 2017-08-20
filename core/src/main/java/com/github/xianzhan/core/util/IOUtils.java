package com.github.xianzhan.core.util;

import java.io.*;
import java.util.Objects;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/8/17
 */
public class IOUtils {

    /**
     * 文件字节复制
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

            // 此处读磁盘, 多少字节读多少次, 效率低下
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            if (Objects.nonNull(in))
                in.close();
            if (Objects.nonNull(out))
                out.close();
        }
    }

    /**
     * 文本文件字符复制
     * 不推荐
     *
     * @param src    源文件
     * @param target 目标文件
     * @throws IOException
     */
    public static void copyCharacters(String src, String target) throws IOException {
        FileReader fr = null;
        FileWriter fw = null;

        try {
            fr = new FileReader(src);
            fw = new FileWriter(target);

            int c;
            // 此处读磁盘, 多少字符读多少次, 效率低下
            while ((c = fr.read()) != -1)
                fw.write(c);
        } finally {
            if (Objects.nonNull(fr))
                fr.close();
            if (Objects.nonNull(fw))
                fw.close();
        }
    }

    /**
     * 缓存复制
     * 推荐
     *
     * @param src    源文件
     * @param target 目标文件
     * @param size   缓存大小, 可减少访问磁盘
     * @throws IOException
     */
    public static void copyBuffers(String src, String target, int size) throws
            IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(target));

            // 缓存
            byte[] b = new byte[size];
            while (bis.read(b) != -1)
                bos.write(b);
        } finally {
            if (Objects.nonNull(bis))
                bis.close();
            if (Objects.nonNull(bos))
                bos.close();
        }
    }
}
