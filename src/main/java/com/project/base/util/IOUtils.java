package com.project.base.util;

import java.io.Closeable;

/**
 * @author yinshaobo at 2020/7/23 16:54
 */
public class IOUtils {

    public static void close(Closeable x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception var2) {
                throw new RuntimeException(var2);
            }
        }

    }
}
