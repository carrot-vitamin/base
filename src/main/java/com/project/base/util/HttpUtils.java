package com.project.base.util;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author yinshaobo
 */
public class HttpUtils extends AbsHttp {

    public static String postBinary(String url, InputStream inputStream) throws Exception {

        HttpURLConnection conn = null;

        String res;

        try{
            conn = buildConnection(url, "POST", null);
            conn.setRequestProperty("Content-Type", "multipart/form-data;");

            res = readResponse(conn, null, inputStream);

        }finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
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

    /**
     * form表单方式获取响应数据流
     * @param url url
     * @param params params
     * @return InputStream
     * @throws Exception e
     */
//    public static InputStream getInputStreamByPost(String url, Map<String, Object> params) throws Exception {
//        return HttpExecuteUtils.executeReturnResponse(url, HttpExecuteUtils.MethodEnum.POST, HttpExecuteUtils.TypeEnum.FORM, params, null).getEntity().getContent();
//    }

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
