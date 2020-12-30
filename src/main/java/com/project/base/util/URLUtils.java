package com.project.base.util;

import java.util.Map;

/**
 * @author za-yinshaobo at 2020/12/30 18:32
 */
public class URLUtils {

    /**
     * map转url参数
     * @param params map
     * @return k1=v1
     */
    public static String mapToUrlParams(Map<?, ?> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<?, ?> entry : params.entrySet()) {
            builder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        return builder.toString().replaceFirst("&", "");
    }

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
        String paramsUrl = mapToUrlParams(params);
        return url.contains("?") ? url + "&" + paramsUrl : url + "?" + paramsUrl;
    }
}
