package com.project.base.util;

import com.project.base.model.exception.CheckException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author za-yinshaobo
 * @date 2020/11/13 14:10
 */
public abstract class AbsHttp {

    protected static HttpURLConnection buildConnection(String url, String method, Map<String, String> headers) throws Exception {
        if (StringUtils.isAllBlank(url, method)) {
            throw new CheckException("400", "url or method is invalid !!!");
        }
        //创建远程url连接对象
        URL u = new URL(url);
        //通过远程url对象打开一个连接，强制转换为HttpUrlConnection类型
        HttpURLConnection conn = (HttpURLConnection)u.openConnection();
        //设置连接方式
        conn.setRequestMethod(method.toUpperCase());
        //设置读取远程返回的数据时间 ms
        conn.setReadTimeout(50000);
        //设置连接主机服务器超时时间 ms
        conn.setConnectTimeout(60000);
        //默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
        conn.setDoInput(true);
        //默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
        conn.setDoOutput(true);
        //设置通用的请求属性
        conn.setRequestProperty("accept","*/*");
        conn.setRequestProperty("connection","Keep-Alive");

        //设置请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return conn;
    }

    protected static String readResponse(HttpURLConnection conn, String body, InputStream inputStream) throws Exception {
        StringBuilder builder = new StringBuilder();
        PrintWriter writer = null;
        BufferedReader reader = null;
        DataInputStream in = null;
        OutputStream out = null;
        try {

            if (StringUtils.isNotBlank(body)) {
                writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
                writer.print(body);
                writer.flush();
            }

            if (inputStream != null) {
                out = new DataOutputStream(conn.getOutputStream());
                in = new DataInputStream(inputStream);
                int bytes;
                byte[] bufferOut = new byte[2048];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
            }

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            String content;
            while((content = reader.readLine())!=null){
                builder.append(content);
            }
        } finally {
            if(writer!=null){
                writer.close();
            }
            if(reader!=null){
                reader.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }

        return builder.toString();
    }
}
