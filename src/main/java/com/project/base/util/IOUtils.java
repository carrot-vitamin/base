package com.project.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * @author yinshaobo
 * 2020/8/27 15:54
 */
public class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    public static void close(Closeable x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
