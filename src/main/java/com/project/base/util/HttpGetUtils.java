package com.project.base.util;

import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpGetUtils extends AbsHttp {

    public static String get(String url) throws Exception {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headers) throws Exception {
        HttpURLConnection conn = null;

        String res;

        try {
            conn = buildConnection(url, "GET", headers);
            res = readResponse(conn, null, null);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

}
