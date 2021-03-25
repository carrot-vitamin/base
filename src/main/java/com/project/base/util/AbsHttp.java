package com.project.base.util;

import com.project.base.model.exception.CheckException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yinshaobo
 */
public abstract class AbsHttp {

    /**
     * 边界标识
     */
    protected final static String BOUNDARY = KeyGenerator.generate();

    private final static String PREFIX = "--";

    private final static String LINE_END = "\r\n";

    protected static HttpURLConnection buildConnection(String url, String method, Map<String, String> headers) throws Exception {
        if (StringUtils.isAllBlank(url, method)) {
            throw new CheckException("400", "url or method is invalid !!!");
        }
        //创建远程url连接对象
        URL u = new URL(url);
        //通过远程url对象打开一个连接，强制转换为HttpUrlConnection类型
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
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
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");

        //设置请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return conn;
    }


    protected static String readResponse(HttpURLConnection conn, String body, Map<String, File> fileMap) throws Exception {
        StringBuilder builder = new StringBuilder();
        PrintWriter writer = null;
        BufferedReader reader = null;
        InputStream in = null;
        OutputStream out = null;
        try {

            if (StringUtils.isNotBlank(body)) {
                writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
                writer.print(body);
                writer.flush();
            }

            if (fileMap != null && !fileMap.isEmpty()) {
                StringBuilder requestParams = new StringBuilder();

                out = new DataOutputStream(conn.getOutputStream());

                for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                    requestParams.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    requestParams.append("Content-Disposition: form-data; name=\"")
                            .append(entry.getKey()).append("\"; filename=\"")
                            .append(entry.getValue().getName()).append("\"")
                            .append(LINE_END);
                    requestParams.append("Content-Type:")
                            .append(getContentType(entry.getValue()))
                            .append(LINE_END);
                    requestParams.append("Content-Transfer-Encoding: 8bit").append(LINE_END);
                    // 参数头设置完以后需要两个换行，然后才是参数内容
                    requestParams.append(LINE_END);
                    out.write(requestParams.toString().getBytes());
                    in = new FileInputStream(entry.getValue());
                    byte[] buffer = new byte[1024 * 1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.write(LINE_END.getBytes());
                    out.flush();
                }

                String endTarget = PREFIX + BOUNDARY + PREFIX + LINE_END;
                out.write(endTarget.getBytes());
                out.flush();
            }




            //获取响应流
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            String content;
            while ((content = reader.readLine()) != null) {
                builder.append(content);
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
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

    private static String getContentType(File file) throws Exception {
        String streamContentType = "application/octet-stream";
        String imageContentType;
        ImageInputStream image = null;
        try {
            image = ImageIO.createImageInputStream(file);
            if (image == null) {
                return streamContentType;
            }
            Iterator<ImageReader> it = ImageIO.getImageReaders(image);
            if (it.hasNext()) {
                imageContentType = "image/" + it.next().getFormatName();
                return imageContentType;
            }
        } finally {
            IOUtils.close(image);
        }
        return streamContentType;
    }

}
