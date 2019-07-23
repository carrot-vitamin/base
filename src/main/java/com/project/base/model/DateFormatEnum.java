package com.project.base.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author yinshaobo on 2019-07-23 12:04
 * 单例时间格式枚举
 */
public enum DateFormatEnum {

    /**
     * yyyyMMddHHmmss
     */
    yyyyMMddHHmmss(new SimpleDateFormat("yyyyMMddHHmmss")),

    /**
     * yyyyMMddHHmmsss
     */
    yyyyMMddHHmmsss(new SimpleDateFormat("yyyyMMddHHmmsss")),

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    yyyy_MM_ddHH_mm_ss(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")),

    /**
     * yyyy-MM-dd HH:mm:sss
     */
    yyyy_MM_ddHH_mm_sss(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss")),

    /**
     * yyyyMMdd
     */
    yyyyMMdd(new SimpleDateFormat("yyyyMMdd")),

    /**
     * yyyy-MM-dd
     */
    yyyy_MM_dd(new SimpleDateFormat("yyyy-MM-dd")),

    /**
     * HHmmss
     */
    HHmmss(new SimpleDateFormat("HHmmss")),

    /**
     * HHmmsss
     */
    HHmmsss(new SimpleDateFormat("HHmmsss")),

    /**
     * HH:mm:ss
     */
    Hms(new SimpleDateFormat("HH:mm:ss")),

    /**
     * HH:mm:sss
     */
    Hmss(new SimpleDateFormat("HH:mm:sss")),
    ;



    private DateFormat dateFormat;

    DateFormatEnum(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }
}
