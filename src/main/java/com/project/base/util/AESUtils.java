package com.project.base.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * @author yin
 * 2018/9/29 23:52
 */
public class AESUtils {

    /**
     * 加密盐值
     */
    private static final String SALT_VALUE = "MG9rbShJSk4JytHf";

    /**
     * 加密明文字符串
     * @param data 要加密的数据
     * @param key 密钥
     * @return 加密后的数据
     */
    public static String encrypt(String key, String data) throws Exception {
        byte[] bytes = encryptBytes(data.getBytes(Charset.defaultCharset()), key.getBytes(Charset.defaultCharset()));
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 解密密文字符串
     * @param data 要解密的数据
     * @param key 密钥
     * @return 解密后的数据
     */
    public static String decrypt(String key, String data) throws Exception {
        byte[] bytes = decryptBytes(Base64.decodeBase64(data), key.getBytes(Charset.defaultCharset()));
        return new String(bytes, Charset.defaultCharset());
    }


    private static byte[] encryptBytes(byte[] data, byte[] keyBytes) throws Exception {
        return cipherOpt(data, keyBytes, Cipher.ENCRYPT_MODE);
    }

    private static byte[] decryptBytes(byte[] data, byte[] keyBytes) throws Exception {
        return cipherOpt(data, keyBytes, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipherOpt(byte[] dataBytes, byte[] keyBytes, int mode) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(SALT_VALUE.getBytes(Charset.defaultCharset()));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);
        return cipher.doFinal(dataBytes);
    }

}
