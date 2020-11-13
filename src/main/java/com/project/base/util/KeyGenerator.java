package com.project.base.util;

import com.project.base.model.enums.DateFormatEnum;

import java.util.Date;
import java.util.UUID;

/**
 * @author yin
 * 2018/9/22 4:17
 */
public class KeyGenerator {

    private static byte[] lock = new byte[0];

    /**
     * 位数，默认是8位
     */
    private final static long W = 100000000;

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成带有时间格式的随机字符串
     *
     * @return 随机字符串
     */
    public static String randomTimestamp() {
        long r;
        synchronized (lock) {
            r = (long) ((Math.random() + 1) * W);
        }
        return DateFormatEnum.yyyyMMddHHmmsss.getDateFormat().format(new Date()) + String.valueOf(r).substring(1);
    }
}
