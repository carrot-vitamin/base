package com.project.base.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpPutUtils {

    /**
     * put
     * @param url url
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String put(String url) throws Exception {
        return putForm(url, null);
    }

    /********************************** PUT FORM ***************************************************/

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

    /**
     * put form
     * @param url url
     * @param object {@link java.lang.Object}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putForm(String url, Object object) throws Exception {
        return putForm(url, ObjectUtils.convertObject2Map(object));
    }

    /********************************** PUT JSON ***************************************************/

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

    /**
     * put json
     * @param url url
     * @param object {@link java.lang.Object}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putJson(String url, Object object) throws Exception {
        return putJson(url, ObjectUtils.convertObject2Map(object));
    }

    /**
     * put json
     * @param url url
     * @param json json
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putJson(String url, String json) throws Exception {
        return putJson(url, json, null);
    }

    /**
     * put json
     * @param url url
     * @param json json
     * @param headers {@link java.util.Map}
     * @return {@link java.lang.String}
     * @throws Exception e
     */
    public static String putJson(String url, String json, Map<String, String> headers) throws Exception {
        Map<String, Object> params = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {});
        return putJson(url, params, headers);
    }
}
