package com.github.xianzhan.core.util.function.file;

import java.io.File;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/7
 */
@FunctionalInterface
public interface FileRead {

    void read(Exception err, File data);
}
