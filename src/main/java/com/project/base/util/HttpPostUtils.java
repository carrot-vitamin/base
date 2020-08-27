package com.project.base.util;


import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpPostUtils {

    /********************************** POST FORM ***************************************************/

    /**
     * post
     * @param url url
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postForm(String url) throws Exception {
        return postForm(url, null);
    }

    /**
     * post form
     * @param url url
     * @param params {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postForm(String url, Map<String, Object> params) throws Exception {
        return postForm(url, params, null);
    }

    /**
     * post form
     * @param url url
     * @param params {@link java.util.Map}
     * @param headers {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postForm(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return HttpExecuteUtils.execute(url, HttpExecuteUtils.MethodEnum.POST, HttpExecuteUtils.TypeEnum.FORM, params, headers);
    }

    /********************************** POST JSON ***************************************************/

    /**
     *
     * @param url url
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postJson(String url) throws Exception {
        return postJson(url, null);
    }

    /**
     * post json
     * @param url url
     * @param params {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postJson(String url, Map<String, Object> params) throws Exception {
        return postJson(url, params, null);
    }

    /**
     * post json
     * @param url url
     * @param params {@link java.util.Map}
     * @param headers {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postJson(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return HttpExecuteUtils.execute(url, HttpExecuteUtils.MethodEnum.POST, HttpExecuteUtils.TypeEnum.JSON, params, headers);
    }
}
