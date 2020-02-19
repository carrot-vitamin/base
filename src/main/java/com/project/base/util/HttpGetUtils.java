package com.project.base.util;

import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpGetUtils {

    public static String get(String url) throws Exception {
        return get(url, null);
    }

    public static String get(String url, Map<String, Object> params) throws Exception {
        return get(url, params, null);
    }

    public static String get(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return HttpExecuteUtils.execute(url, HttpExecuteUtils.MethodEnum.GET, null, params, headers);
    }

}
