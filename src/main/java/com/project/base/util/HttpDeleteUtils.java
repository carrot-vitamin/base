package com.project.base.util;

import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpDeleteUtils {

    public static String delete(String url) throws Exception {
        return delete(url, null);
    }

    public static String delete(String url, Map<String, Object> params) throws Exception {
        return delete(url, params, null);
    }

    public static String delete(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return HttpExecuteUtils.execute(url, HttpExecuteUtils.MethodEnum.DELETE, null, params, headers);
    }

}
