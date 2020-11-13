package com.project.base.util;

import java.text.MessageFormat;

/**
 * @author Shaobo Yin
 * 2020/7/12 15:10
 */
public class StringUtils {

    /**
     * 占位符替换
     * @param message 你好{}，我是{}
     * @param s 小明, Jane
     * @return 你好小明，我是Jane
     */
    public static String convertMessage(String message, Object... s) {
        String [] arrays = message.split("\\{\\}");
        for (int i = 0; i < arrays.length; i++) {
            message = message.replaceFirst("\\{\\}", "{" + i + "}");
        }
        return MessageFormat.format(message, s);
    }

    /**
     * 统计字符串中包含某个字符串的个数
     * @param str 字符串
     * @param s 要统计的字符串
     * @return 个数
     */
    public static int countChar(String str, String s) {
        int count = 0;
        while (str.contains(s)) {
            str = str.substring(str.indexOf(s) + 1);
            ++count;
        }
        return count;
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllBlank(final CharSequence... css) {
        if (css == null || css.length == 0) {
            return true;
        }
        for (final CharSequence cs : css) {
            if (isNotBlank(cs)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

}
