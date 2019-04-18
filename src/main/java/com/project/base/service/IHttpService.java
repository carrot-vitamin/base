package com.project.base.service;

import org.apache.http.HttpResponse;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public interface IHttpService {

    String get(String url) throws Exception;

    String get(String url, Map<String, Object> params) throws Exception;

    String post(String url) throws Exception;

    String post(String url, Map<String, Object> params) throws Exception;

    String postJson(String url, String json) throws Exception;

    String postJson(String url, Map<String, Object> params) throws Exception;

    InputStream getInputStreamByPost(String url, Map<String, Object> params) throws Exception;

    HttpResponse getHttpResponseByPost(String url, Map<String, Object> params) throws Exception;

    File getFileByPost(String url, Map<String, Object> params, String localFile, String suffixName) throws Exception;
}
