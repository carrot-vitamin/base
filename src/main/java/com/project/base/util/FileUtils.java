package com.project.base.util;

import com.alibaba.fastjson.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author
 * @ClassName: FileUtils
 * @Description:
 * @date: 2018/12/28 10:06 AM
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static ClassLoader classLoader = FileUtils.class.getClassLoader();

    /**
     * 获取resources文件夹下文件的路径
     * @param fileName e.g. config/application.properties
     * @return
     */
    public static String getResourceFilePath(String fileName) {
        String path = null;
        URL url = classLoader.getResource(fileName);
        if (url != null) {
            path = url.getPath();
        } else {
            logger.warn("can't get file by file name={}", fileName);
        }
        return path;
    }

    /**
     * 获取resources文件夹下Properties对象
     * @param propertiesName properties文件名 e.g. config/application.properties
     * @return
     */
    public static Properties getProperties(String propertiesName) {
        Properties properties = null;
        InputStream inputStream = null;
        try {
            inputStream = classLoader.getResourceAsStream(propertiesName);
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception e) {
            logger.error("get Properties error, properties name={}", propertiesName, e);
        } finally {
            IOUtils.close(inputStream);
        }
        return properties;
    }

    /**
     * 根据key获取properties file value
     * @param key
     * @param propertiesName e.g. config/application.properties
     * @return
     */
    public static String getPropertiesValue(String key, String propertiesName) {
        String value = null;
        Properties properties = getProperties(propertiesName);
        if (properties != null) {
            value = properties.getProperty(key);
        } else {
            logger.warn("can't get Properties object, properties name={}", propertiesName);
        }
        return value;
    }
}
