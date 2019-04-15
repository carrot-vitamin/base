package com.project.base.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author yinshaobo
 * @date 2018/9/20 下午6:28
 */
public class Base64Utils {

    /**
     * 文件转为Base64
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
            e.printStackTrace();
            return null;
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        //返回Base64编码过的字节数组字符串
        return encoder.encode(data);

    }

    /**
     * 将Base64转为文件
     * @param base64 base64
     * @param localPath 本地文件
     * @return boolean
     */
    public static boolean base642File(String base64, String localPath) {
        // 图像数据为空
        if (base64 == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(base64);
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
     * @param string 要编码的字符串
     * @return 编码后的字符串
     */
    public static String encode(String string) {
        return Base64.encodeBase64URLSafeString(string.getBytes(Charset.defaultCharset()));
    }

    /**
     * Base64解码
     * @param string 编码后的字符串
     * @return 解码后的字符串
     */
    public static String decode(String string) {
        return new String(Base64.decodeBase64(string), Charset.defaultCharset());
    }
}
