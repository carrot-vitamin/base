package com.project.base.util;

import com.project.base.model.exception.CheckException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author yinshaobo
 */
public class HttpUtils {

    public static String postBinary(String url, Map<String, Object> params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(HttpExecuteUtils.getConfig());
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
        httpPost.addHeader("Content-Type", "multipart/form-data;boundary=" + httpEntity.getContentLength());
        httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        HttpResponse httpResponse = HttpExecuteUtils.getHttpClient().execute(httpPost);
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

    /**
     * form表单方式获取响应数据流
     * @param url url
     * @param params params
     * @return InputStream
     * @throws Exception e
     */
    public static InputStream getInputStreamByPost(String url, Map<String, Object> params) throws Exception {
        return HttpExecuteUtils.executeReturnResponse(url, HttpExecuteUtils.MethodEnum.POST, HttpExecuteUtils.TypeEnum.FORM, params, null).getEntity().getContent();
    }

    private static boolean responseOK(HttpResponse response) {
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }

    private static String getResult(HttpResponse response) throws Exception {
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        if (responseOK(response)) {
            return data;
        }
        throw new CheckException(String.valueOf(response.getStatusLine().getStatusCode()), data);
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
