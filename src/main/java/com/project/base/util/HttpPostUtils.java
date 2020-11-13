package com.project.base.util;


import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpPostUtils extends AbsHttp {

    public static String post(String url) throws Exception {
        HttpURLConnection conn = null;

        String res;

        try {
            conn = buildConnection(url, "POST", null);
            res = readResponse(conn, null, null);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    /********************************** POST FORM ***************************************************/

    /**
     * @param url        请求地址
     * @param bodyParams 请求body数据 k1=v1&k2=v2... ...
     * @return response info
     * @throws Exception e
     */
    public static String postForm(String url, String bodyParams) throws Exception {
        return postForm(url, bodyParams, null);
    }

    /**
     * @param url        请求地址
     * @param bodyParams 请求body数据 k1=v1&k2=v2... ...
     * @param headers    请求头信息
     * @return response info
     * @throws Exception e
     */
    public static String postForm(String url, String bodyParams, Map<String, String> headers) throws Exception {

        if (StringUtils.isBlank(bodyParams)) {
            throw new RuntimeException("request body is empty!");
        }

        HttpURLConnection conn = null;

        String res;

        try {
            conn = buildConnection(url, "POST", headers);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            res = readResponse(conn, bodyParams, null);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    /********************************** POST JSON ***************************************************/

    /**
     * @param url  请求地址
     * @param json 请求json数据
     * @return response info
     * @throws Exception e
     */
    public static String postJson(String url, String json) throws Exception {
        return postJson(url, json, null);
    }

    /**
     * @param url     请求地址
     * @param json    请求json数据
     * @param headers 请求头信息
     * @return response info
     * @throws Exception e
     */
    public static String postJson(String url, String json, Map<String, String> headers) throws Exception {

        if (StringUtils.isBlank(json)) {
            throw new RuntimeException("json body is empty!");
        }

        HttpURLConnection conn = null;

        String res;

        try {
            conn = buildConnection(url, "POST", headers);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            res = readResponse(conn, json, null);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }
}
