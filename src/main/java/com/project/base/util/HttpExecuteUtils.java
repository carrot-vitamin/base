package com.project.base.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpExecuteUtils {

    private HttpExecuteUtils() {}

    protected enum MethodEnum {
        GET, POST, PUT, DELETE
    }

    protected enum TypeEnum {
        FORM, JSON
    }

    /**
     * 设置链接超时时间为60s
     */
    private static final int TIME_OUT = 60 * 1000;

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static RequestConfig config = RequestConfig.custom()
            .setConnectionRequestTimeout(TIME_OUT)
            .setConnectTimeout(TIME_OUT)
            .setSocketTimeout(TIME_OUT).build();

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static RequestConfig getConfig() {
        return config;
    }

    /**
     * 通用请求
     * @param url url
     * @param method {@link MethodEnum}
     * @param type {@link TypeEnum} 仅在 POST 或 PUT 时有效
     * @param params params
     * @param headers headers
     * @return res
     * @throws Exception e
     */
    public static String execute(String url, MethodEnum method, TypeEnum type, Map<String, Object> params, Map<String, String> headers) throws Exception {
        HttpResponse response = executeReturnResponse(url, method, type, params, headers);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }
        throw new Exception(String.valueOf(response.getStatusLine().getStatusCode()));
    }

    public static HttpResponse executeReturnResponse(String url, MethodEnum method, TypeEnum type, Map<String, Object> params, Map<String, String> headers) throws Exception {
        HttpRequestBase requestBase;
        if (MethodEnum.GET == method || MethodEnum.DELETE == method) {
            URIBuilder uriBuilder = uriBuilder(url, params);
            url = uriBuilder.toString();
            requestBase = MethodEnum.GET == method ? new HttpGet(url) : new HttpDelete(url);
        } else {
            HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = MethodEnum.POST == method ? new HttpPost(url) : new HttpPut(url);
            if (TypeEnum.FORM == type) {
                //form表单提交
                //封装form表单对象
                if (params != null && !params.isEmpty()) {
                    List<NameValuePair> list = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                    }
                    // 构造from表单对象
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
                    // 把表单放到post里
                    httpEntityEnclosingRequestBase.setEntity(urlEncodedFormEntity);
                }
            } else {
                //JSON提交
                StringEntity entity = new StringEntity(JSON.toJSONString(params), Charset.forName("UTF-8"));
                entity.setContentType("application/json");
                httpEntityEnclosingRequestBase.setEntity(entity);
            }

            requestBase = httpEntityEnclosingRequestBase;
        }

        // 装载配置信息
        requestBase.setConfig(config);

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBase.addHeader(entry.getKey(), entry.getValue());
            }
        }
        // 发起请求
        return httpClient.execute(requestBase);
    }

    /**
     * url参数组装
     * @param url url
     * @param params params
     * @return url?a=b
     * @throws Exception e
     */
    private static URIBuilder uriBuilder(String url, Map<String, Object> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            // 遍历map,拼接请求参数
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        return uriBuilder;
    }

}
