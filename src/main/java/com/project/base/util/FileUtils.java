package com.project.base.util;

import com.alibaba.fastjson.util.IOUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @ClassName: FileUtils
 * @Description:
 * @author: ex-yinshaobo001
 * @date: 2018/12/28 10:06 AM
 */
@Slf4j
public class FileUtils {

    private static ClassLoader classLoader = FileUtils.class.getClassLoader();

    /**
     * 获取resources文件夹下文件的路径
     *
     * @param fileName e.g. config/application.properties
     * @return
     */
    public static String getResourceFilePath(String fileName) {
        String path = null;
        URL url = classLoader.getResource(fileName);
        if (url != null) {
            path = url.getPath();
        } else {
            log.warn("can't get file by file name={}", fileName);
        }
        return path;
    }

    /**
     * 获取resources文件夹下Properties对象
     *
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
            log.error("get Properties error, properties name={}", propertiesName, e);
        } finally {
            IOUtils.close(inputStream);
        }
        return properties;
    }

    /**
     * 根据key获取properties file value
     *
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
            log.warn("can't get Properties object, properties name={}", propertiesName);
        }
        return value;
    }

    /**
     * 删除文件（夹）
     *
     * @param folderPath
     */
    public static boolean deleteFile(String folderPath) {
        //删除完里面所有内容
        delAllFile(folderPath);
        File myFilePath = new File(folderPath);
        //删除空文件夹
        return myFilePath.delete();
    }

    /**
     * 删除文件夹下所有文件
     *
     * @param path
     * @return
     */
    private static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        if (tempList == null) {
            return false;
        }
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                //先删除文件夹里面的文件
                delAllFile(path + "/" + tempList[i]);
                //再删除空文件夹
                deleteFile(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }
}
