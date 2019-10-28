package com.project.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ex-yinshaobo001
 * 2018/11/30 2:32 PM
 */
public class DateUtils {

    private static Logger log = LoggerFactory.getLogger(DateUtils.class);

    private static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 计算两时间间隔
     * @param startDate 开始时间
     * @param endDate 结束四件
     * @return 时间间隔，单位：天
     */
    public static long getInterval(Date startDate, Date endDate) {
        try {
            startDate = defaultDateFormat.parse(defaultDateFormat.format(startDate));
            endDate = defaultDateFormat.parse(defaultDateFormat.format(endDate));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return (endDate.getTime() - startDate.getTime()) / (24 * 3600 * 1000);
    }

    /**
     * 判断当天是周几
     * @param date java.util.Date
     * @return string
     */
    public static String getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String week = null;
        if (day == 1) {
            week = "周日";
        } else if (day == 2) {
            week = "周一";
        } else if (day == 3) {
            week = "周二";
        } else if (day == 4) {
            week = "周三";
        } else if (day == 5) {
            week = "周四";
        } else if (day == 6) {
            week = "周五";
        } else if (day == 7) {
            week = "周六";
        }
        return week;
    }

    /**
     * 判断当天是周几
     * @param date yyyy-MM-dd
     * @return string
     */
    public static String getWeekDay(String date) {
        try {
            return getWeekDay(defaultDateFormat.parse(date));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 判断当天是否为工作日（周一到周五）
     * @param date date
     * @return boolean
     */
    public static boolean isWorkDay(Date date) {
        String weekDay = getWeekDay(date);
        return !"周六".equals(weekDay) && !"周日".equals(weekDay);
    }

    /**
     * 判断日期是否为周六或周日
     * @param date date
     * @return boolean
     */
    public static boolean isWeekend(Date date) {
        return !isWorkDay(date);
    }

    /**
     * 将指定日期增加指定天数
     * @param date date
     * @param days days
     * @return Date
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 计算两个时间的小时差
     * @param start 开始时间
     * @param end 结束时间
     * @return 小时级别的差
     */
    public static long hourDisparity(Date start, Date end) {
        return (end.getTime() - start.getTime()) / (60 * 60 * 1000);
    }

}
