package com.project.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    /**
     * form表单方式提交对象
     * @param url 请求URL地址
     * @param object 要提交的参数对象
     * @return 响应结果
     * @throws Exception Exception
     */
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

    public static String postBinary(String url, Map<String, Object> params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        //解决中文乱码的问题
        ContentType contentType = ContentType.create("text/plain", Consts.UTF_8);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object object = entry.getValue();
            if (object instanceof String) {
                /*multipartEntityBuilder.addPart(entry.getKey(), new StringBody((String) entry.getValue(), ContentType.TEXT_PLAIN));*/
                multipartEntityBuilder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()), contentType);
            } else if (object instanceof File) {
                /*multipartEntityBuilder.addBinaryBody(entry.getKey(), file, ContentType.create("image/png"),"abc.pdf");*/
                /*当设置了setSocketTimeout参数后，以下代码上传PDF不能成功，将setSocketTimeout参数去掉后此可以上传成功。上传图片则没有个限制*/
                multipartEntityBuilder.addBinaryBody(entry.getKey(), (File) object);
            } else if (object instanceof InputStream) {
                multipartEntityBuilder.addBinaryBody(entry.getKey(), (InputStream) object);
            } else if (object instanceof byte []) {
                multipartEntityBuilder.addBinaryBody(entry.getKey(), (byte []) object);
            }
        }
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);
        httpPost.addHeader("Connection", "keep-alive");
        httpPost.addHeader("Accept", "*/*");
        httpPost.addHeader("Content-Type", "multipart/form-data;boundary=" + new Random().nextLong());
        httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        return getResult(httpResponse);
    }

    /**
     * 通过get请求得到读取器响应数据的数据流
     * @param url 请求URL地址
     * @return 获取的文件流
     * @throws Exception Exception
     */
    public static InputStream getInputStreamByGet(String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        throw new Exception("HttpUtils.getInputStreamByGet error");
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

    /**
     * 以GET方式从网络下载文件流并保存为本地文件
     * @param url 网络URL
     * @param localFilePath 本地文件路径
     * @param fileName 文件名
     * @return File对象
     * @throws Exception Exception
     */
    public static File getFileByGet(String url, String localFilePath, String fileName) throws Exception {
        InputStream inputStream = getInputStreamByGet(url);
        return FileUtils.saveFileByInputStream(inputStream, localFilePath, fileName);
    }

}
