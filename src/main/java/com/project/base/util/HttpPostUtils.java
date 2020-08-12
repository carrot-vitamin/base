package com.project.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpPostUtils {

    /**
     * post
     * @param url url
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String post(String url) throws Exception {
        return postForm(url, null);
    }

    /********************************** POST FORM ***************************************************/

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

    /**
     * post form
     * @param url url
     * @param object {@link java.lang.Object}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postForm(String url, Object object) throws Exception {
        return postForm(url, ObjectUtils.convertObject2Map(object));
    }

    /********************************** POST JSON ***************************************************/

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

    /**
     * post json
     * @param url url
     * @param object {@link java.lang.Object}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postJson(String url, Object object) throws Exception {
        return postJson(url, ObjectUtils.convertObject2Map(object));
    }

    /**
     * post json
     * @param url url
     * @param json json
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postJson(String url, String json) throws Exception {
        return postJson(url, json, null);
    }

    /**
     * post json
     * @param url url
     * @param json json
     * @param headers {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String postJson(String url, String json, Map<String, String> headers) throws Exception {
        Map<String, Object> params = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {});
        return postJson(url, params, headers);
    }
}
