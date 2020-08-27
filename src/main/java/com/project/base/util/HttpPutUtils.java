package com.project.base.util;

import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpPutUtils {

    /********************************** PUT FORM ***************************************************/

    /**
     * put
     * @param url url
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putForm(String url) throws Exception {
        return putForm(url, null);
    }

    /**
     * put form
     * @param url url
     * @param params {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putForm(String url, Map<String, Object> params) throws Exception {
        return putForm(url, params, null);
    }

    /**
     * put form
     * @param url url
     * @param params {@link java.util.Map}
     * @param headers {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putForm(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return HttpExecuteUtils.execute(url, HttpExecuteUtils.MethodEnum.PUT, HttpExecuteUtils.TypeEnum.FORM, params, headers);
    }

    /********************************** PUT JSON ***************************************************/

    /**
     *
     * @param url url
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putJson(String url) throws Exception {
        return putJson(url, null);
    }

    /**
     * put json
     * @param url url
     * @param params {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putJson(String url, Map<String, Object> params) throws Exception {
        return putJson(url, params, null);
    }

    /**
     * put json
     * @param url url
     * @param params {@link java.util.Map}
     * @param headers {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putJson(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return HttpExecuteUtils.execute(url, HttpExecuteUtils.MethodEnum.PUT, HttpExecuteUtils.TypeEnum.JSON, params, headers);
    }
}
