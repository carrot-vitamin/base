package com.project.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author yin
 * 2018/9/29 23:52
 */
public class AESUtils {

    private static Logger log = LoggerFactory.getLogger(AESUtils.class);

    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据aesKey初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     *
     * @param aesKey  aesKey
     * @param content content
     * @return string
     */
    public static String encrypt(String aesKey, String content) {
        try {
            Object[] objects = getCipher(aesKey);
            Cipher cipher = (Cipher) objects[0];
            SecretKey key = (SecretKey) objects[1];
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byteEncode = content.getBytes(StandardCharsets.UTF_8);
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byteAES = cipher.doFinal(byteEncode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            return Base64.getEncoder().encodeToString(byteAES);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 解密
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     *
     * @param aesKey  aesKey
     * @param content content
     * @return string
     */
    public static String decrypt(String aesKey, String content) {
        try {
            Object[] objects = getCipher(aesKey);
            Cipher cipher = (Cipher) objects[0];
            SecretKey key = (SecretKey) objects[1];
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte[] byteContent = Base64.getDecoder().decode(content);
            /*
             * 解密
             */
            byte[] byteDecode = cipher.doFinal(byteContent);
            return new String(byteDecode, "utf-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private static Object[] getCipher(String aesKey) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(aesKey.getBytes()));
            //3.产生原始对称密钥
            SecretKey secretKey = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = secretKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            return new Object[]{cipher, key};
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
