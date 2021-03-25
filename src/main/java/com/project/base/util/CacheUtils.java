package com.project.base.util;

import com.project.base.model.ExpireObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yinshaobo at 2021/1/4 14:48
 * 将内容存储至文件
 */
public class CacheUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);

    private static final String CACHE_PREFIX = "cache_prefix_";

    /**
     * 文件存储路径默认为当前项目路径
     */
    private static String filePath = new File("").getAbsolutePath();

    /**
     * 重设缓存文件路径
     * @param path 缓存文件路径
     */
    public static void resetFilePath(String path) {
        filePath = path;
    }

    /**
     * 缓存数据
     * @param key key
     * @param t v
     * @param seconds 缓存时间，单位：秒
     * @param <T> Serializable
     * @return result
     */
    public static <T extends Serializable> boolean set(String key, T t, int seconds) {
        boolean result;
        try {
            Date expire = addSeconds(seconds);
            String url = buildFilePath(key);
            LOGGER.info("缓存文件路径：{}", url);
            ExpireObject<T> object = new ExpireObject<>(key, t, expire);
            SerializationUtils.serialize(object, new FileOutputStream(url));

            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = false;
        }

        return result;
    }

    /**
     * 获取缓存
     * @param key key
     * @param <T> Object
     * @return 缓存的Object
     */
    public static <T extends Serializable> T get(String key) {
        ExpireObject<T> t = new ExpireObject<>();
        try {
            String url = buildFilePath(key);
            if (new File(url).exists()) {
                t = SerializationUtils.deserialize(new FileInputStream(url));
                if (new Date().after(t.getExpireTime())) {
                    LOGGER.info("缓存中的数据已经失效... ...缓存数据=【{}】", t);
                    t.setT(null);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return t.getT();
    }

    /**
     * 设置缓存key失效时间
     * @param key key
     * @param seconds 失效时间，单位：秒
     * @return result
     */
    public static boolean expire(String key, int seconds) {
        boolean result = false;
        String url = buildFilePath(key);
        if (new File(url).exists()) {
            try {
                ExpireObject<?> o = SerializationUtils.deserialize(new FileInputStream(url));
                o.setExpireTime(addSeconds(seconds));
                SerializationUtils.serialize(o, new FileOutputStream(url));
                result = true;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private static String buildFilePath(String key) {
        return filePath + File.separator + (CACHE_PREFIX + key);
    }

    private static Date addSeconds(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }
}
