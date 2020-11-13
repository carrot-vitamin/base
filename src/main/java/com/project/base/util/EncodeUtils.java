package com.project.base.util;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yinshaobo on 2019-08-22 16:48
 * 编码工具类
 */
public class EncodeUtils {

    private static final Pattern PATTERN = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

    /**
     * unicode转字符串
     *
     * @param str unicode编码字符串
     * @return utf-8字符串
     */
    public static String unicodeToString(String str) {
        Matcher matcher = PATTERN.matcher(str);
        char ch;
        while (matcher.find()) {
            String group = matcher.group(2);
            ch = (char) Integer.parseInt(group, 16);
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return new String(str.getBytes(), Charset.defaultCharset());
    }
}
