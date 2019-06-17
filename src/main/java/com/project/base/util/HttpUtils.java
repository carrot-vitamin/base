package com.project.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private static CloseableHttpClient httpClient;

    private static RequestConfig config;

    static {
        httpClient = HttpClients.createDefault();
        config = RequestConfig.custom()
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(3000)
                .setSocketTimeout(3000).build();
    }

    public static String get(String url) throws Exception {
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);
        // 装载配置信息
        httpGet.setConfig(config);
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return getResult(response);
    }

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

    public static String post(String url) throws Exception {
        return post(url, null);
    }

    /**
     * form表单提交
     * @param url 请求url
     * @param params 请求map参数
     * @return 相应数据
     * @throws Exception exception
     */
    public static String post(String url, Map<String, Object> params) throws Exception {
        HttpResponse response = getHttpResponseByPost(url, params);
        return getResult(response);
    }

    public static String post(String url, Object object) throws Exception {
        return post(url, JSONObject.parseObject(JSON.toJSONString(object), Map.class));
    }

    public static String postJson(String url, String json) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        StringEntity entity = new StringEntity(json, Charset.forName("utf-8"));
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        return getResult(response);
    }

    public static String postJson(String url, Map<String, Object> params) throws Exception {
        return postJson(url, JSON.toJSONString(params));
    }

    public static InputStream getInputStreamByPost(String url, Map<String, Object> params) throws Exception {
        return getHttpResponseByPost(url, params).getEntity().getContent();
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

    public static HttpResponse getHttpResponseByPost(String url, Map<String, Object> params) throws Exception {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        // 加入配置信息
        httpPost.setConfig(config);
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
        return httpClient.execute(httpPost);
    }

    public static File getFileByPost(String url, Map<String, Object> params, String localFilePath, String fileName) throws Exception {
        InputStream inputStream = getInputStreamByPost(url, params);
        return FileUtils.saveFileByInputStream(inputStream, localFilePath, fileName);
    }
}
