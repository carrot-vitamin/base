package com.project.base.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpUtils extends AbsHttp {

    private static CloseableHttpClient httpClient;

    private static RequestConfig requestConfig;

    static {
        //初始化HttpClient
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //最大连接数
        httpClientConnectionManager.setMaxTotal(100);
        //并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(20);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);

        httpClient = httpClientBuilder.build();

        //初始化配置
        RequestConfig.Builder builder = RequestConfig.custom();
        requestConfig = builder.setConnectTimeout(1000)
                .setConnectionRequestTimeout(500)
                .setSocketTimeout(10000)
                .setStaleConnectionCheckEnabled(true)
                .build();
    }

    private static boolean responseOK(HttpResponse response) {
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }

    private static String getResult(HttpResponse response) throws Exception {
        if (responseOK(response)) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }
        throw new Exception(String.valueOf(response.getStatusLine().getStatusCode()));
    }

    /**
     * GET请求
     * @param url url
     * @return str
     * @throws Exception e
     */
    public static String get(String url) throws Exception {
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);
        // 装载配置信息
        httpGet.setConfig(requestConfig);
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return getResult(response);
    }

    /**
     * GET请求
     * @param url url
     * @param params 参数
     * @return str
     * @throws Exception e
     */
    public static String get(String url, Map<String, Object> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            // 遍历map,拼接请求参数
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        // 调用不带参数的get请求
        return get(uriBuilder.build().toString());
    }

    /**
     * POST请求
     * @param url url
     * @return str
     * @throws Exception e
     */
    public static String post(String url) throws Exception {
        return post(url, null);
    }

    /**
     * POST请求
     * @param url url
     * @param params 参数
     * @return str
     * @throws Exception e
     */
    public static String post(String url, Map<String, Object> params) throws Exception {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        // 加入配置信息
        httpPost.setConfig(requestConfig);
        // 判断map是否为空，不为空则进行遍历，封装form表单对象
        if (params != null) {
            List<NameValuePair> list = new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造from表单对象
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
            // 把表单放到post里
            httpPost.setEntity(urlEncodedFormEntity);
        }
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        return getResult(response);
    }

    /**
     * POST JSON
     * @param url url
     * @param params 参数
     * @return str
     * @throws Exception e
     */
    public static String postJson(String url, Map<String, Object> params) throws Exception {
        return postJson(url, JSONUtils.toJson(params));
    }

    /**
     * POST JSON
     * @param url url
     * @param json json
     * @return str
     * @throws Exception e
     */
    public static String postJson(String url, String json) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        return getResult(response);
    }

    public static String postBinary(String url, Map<String, File> fileMap) throws Exception {

        HttpURLConnection conn = null;

        String res;

        try {
            conn = buildConnection(url, "POST", null);
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

            res = readResponse(conn, null, fileMap);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    /**
     * 请求得到读取器响应数据的数据流
     *
     * @param url 请求URL地址
     * @param method 请求方法 GET | POST
     * @return 获取的文件流
     * @throws Exception Exception
     */
    public static InputStream getInputStream(String url, String method) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        conn.setRequestMethod(method.toUpperCase());
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        throw new Exception("HttpUtils.getInputStreamByGet error");
    }

    /**
     * 以GET方式从网络下载文件流并保存为本地文件
     *
     * @param url           网络URL
     * @param localFilePath 本地文件路径
     * @param fileName      文件名
     * @return File对象
     * @throws Exception Exception
     */
    public static File getFileByGet(String url, String localFilePath, String fileName) throws Exception {
        InputStream inputStream = getInputStream(url, "get");
        return FileUtils.saveFileByInputStream(inputStream, localFilePath, fileName);
    }

}
