package com.project.base.util;

import com.project.base.model.exception.CheckException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpExecuteUtils {

    private HttpExecuteUtils() {}

    protected enum MethodEnum {
        /**
         *
         */
        GET, POST, PUT, DELETE
    }

    protected enum TypeEnum {
        /**
         *
         */
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
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return data;
        }
        throw new CheckException(String.valueOf(response.getStatusLine().getStatusCode()), data);
    }

    public static HttpResponse executeReturnResponse(final String url, MethodEnum method, TypeEnum type,
                                                     Map<String, Object> bodyParams, Map<String, String> headers) throws Exception {
        HttpEntityEnclosingRequestBase requestBase = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return method.toString().toLowerCase();
            }

            @Override
            public URI getURI() {
                try {
                    boolean uriParam = (MethodEnum.GET == method || MethodEnum.DELETE == method)
                            && (bodyParams != null && !bodyParams.isEmpty());
                    if (uriParam) {
                        return URI.create(uriBuilder(url, bodyParams).toString());
                    }
                    return URI.create(url);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public RequestConfig getConfig() {
                return config;
            }

            @Override
            public HttpEntity getEntity() {
                if (bodyParams != null && !bodyParams.isEmpty()) {
                    if (TypeEnum.FORM == type) {
                        //form表单请求
                        List<NameValuePair> list = new ArrayList<>();
                        bodyParams.forEach((k, v) -> list.add(new BasicNameValuePair(k, v.toString())));

                        // 构造from表单对象
                        try {
                            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                            entity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
                            return entity;
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (TypeEnum.JSON == type) {
                        //json请求
                        StringEntity entity = new StringEntity(JSONUtils.toJson(bodyParams), StandardCharsets.UTF_8);
                        entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                        return entity;
                    }
                }

                return super.getEntity();
            }
        };

        if (headers != null && !headers.isEmpty()) {
            headers.forEach(requestBase::setHeader);
        }

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
