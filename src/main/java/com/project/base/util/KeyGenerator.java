package com.project.base.util;

import java.util.UUID;

/**
 * @author yin
 * @desc KeyGenerator
 * @date 2018/9/22 4:17
 */
public class KeyGenerator {

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
