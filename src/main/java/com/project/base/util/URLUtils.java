package com.project.base.util;

import java.util.Map;

/**
 * @author za-yinshaobo at 2020/12/30 18:32
 */
public class URLUtils {

    /**
     * URL追加参数
     * @param url url
     * @param params params
     * @return url
     */
    public static String appendParams(String url, Map<?, ?> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        for (Map.Entry<?, ?> entry : params.entrySet()) {
            builder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }

        return builder.toString().replaceFirst("&", "?");
    }
}
