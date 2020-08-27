package com.project.base.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author yinshaobo
 * 2018/9/20 下午6:28
 */
public class Base64Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Base64Utils.class);

    /**
     * 文件转为Base64
     *
     * @param filePath 本地文件路径
     * @return string
     */
    public static String file2Base64(String filePath) {
        InputStream in;
        byte[] data;

        // 读取图片字节数组
        try {
            in = new FileInputStream(filePath);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        // 对字节数组Base64编码
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        //返回Base64编码过的字节数组字符串
        return encoder.encodeToString(data);

    }

    /**
     * 将Base64转为文件
     *
     * @param base64    base64
     * @param localPath 本地文件
     * @return boolean
     */
    public static boolean base642File(String base64, String localPath) {
        // 图像数据为空
        if (base64 == null) {
            return false;
        }
        try {
            // Base64解码
            java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
            byte[] b = decoder.decode(base64);
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 生成文件
            OutputStream out = new FileOutputStream(localPath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Base64编码
     *
     * @param string 要编码的字符串
     * @return 编码后的字符串
     */
    public static String encode(String string) {
        return Base64.encodeBase64URLSafeString(string.getBytes(Charset.defaultCharset()));
    }

    /**
     * Base64解码
     *
     * @param string 编码后的字符串
     * @return 解码后的字符串
     */
    public static String decode(String string) {
        return new String(Base64.decodeBase64(string), Charset.defaultCharset());
    }

    /**
     * 将流转为base64
     *
     * @param inputStream 二进制流
     * @return base64字符串
     */
    private String binary2Base64(InputStream inputStream) {
        String base64 = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer;
            int len;
            byte[] buf = new byte[2048];
            while ((len = inputStream.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            baos.flush();
            buffer = baos.toByteArray();
            base64 = Base64.encodeBase64String(buffer);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            IOUtils.close(baos);
            IOUtils.close(inputStream);
        }
        return base64;
    }
}
