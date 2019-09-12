package com.project.base.util;

import java.io.*;

/**
 * @author yinshaobo on 2019-08-01 13:27
 */
public class SerializeUtils {

    /**
     * 序列化
     * @param object 要序列化的对象
     * @return byte数组
     * @throws Exception Exception
     */
    public static byte [] serialize(Object object) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        byte [] bytes = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return bytes;
    }

    /**
     * 反序列化
     * @param bytes 经序列化后的byte数组
     * @return 反序列化后的对象
     * @throws Exception Exception
     */
    public static Object deSerialize(byte [] bytes) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }
}
