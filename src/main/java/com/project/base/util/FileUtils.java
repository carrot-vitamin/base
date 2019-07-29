package com.project.base.util;

import com.alibaba.fastjson.util.IOUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @author ex-yinshaobo001
 * 2018/12/28 10:06 AM
 */
public class FileUtils {

    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    private static ClassLoader classLoader = FileUtils.class.getClassLoader();

    /**
     * 获取resources文件夹下文件的路径
     *
     * @param fileName e.g. config/application.properties
     * @return String
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
     * @return Properties
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
     * @param key key
     * @param propertiesName e.g. config/application.properties
     * @return String
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
     * @param folderPath 要删除的文件夹路径
     * @return 操作结果
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
     * @param path 要删除的文件夹路径
     * @return 操作结果
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

    /**
     * 将流转换为本地文件，以时间戳自动命名
     * @param inputStream 文件流
     * @param localFilePath 要保存的本地路径，如 /Users/file/voice/
     * @param fileName 文件名
     * @return File对象
     * @throws Exception IOException
     */
    public static File saveFileByInputStream(InputStream inputStream, String localFilePath, String fileName) throws Exception {
        File file = new File(localFilePath + fileName);
        createFile(file);
        OutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int readLength;
        while ((readLength = inputStream.read(buffer)) > 0) {
            byte[] bytes = new byte[readLength];
            System.arraycopy(buffer, 0, bytes, 0, readLength);
            out.write(bytes);
        }
        out.flush();
        out.close();
        return file;
    }

    /**
     * 创建文件，不存在则新建
     * @param file File对象
     * @return 操作结果
     */
    public static boolean createFile(File file) {
        try {
            if (!file.exists() && !file.isDirectory()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                return file.createNewFile();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 根据网络地址解析后缀名
     * @param url 网络URL地址
     * @return 解析后的后缀名
     */
    public static String getSuffixNameByURL(String url) {
        String[] split = url.split("/");
        return split[split.length - 1];
    }

    /**
     * 计算文件大小 单位：字节 (kb)
     * @param base64 base64字符串
     * @return 文件大小 单位：字节 (kb)
     */
    public static Long calcFileProperty(String base64) {
        Integer size = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(base64)) {
            InputStream inputStream = null;
            try {
                byte[] b = Base64.decodeBase64(base64);
                inputStream = new ByteArrayInputStream(b);
                size = inputStream.available();
            }catch(Exception e) {
                log.error(e.getMessage(), e);
            }finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return size != null ? size.longValue() : null;
    }

    public static String readTextContent(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            IOUtils.close(bufferedReader);
            IOUtils.close(fileReader);
            return builder.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将内容写入指定文件，文件不存在则新建
     * @param content 要写入的内容
     * @param filePath 要写入的文件路径
     * @return 写入结果
     */
    public static boolean writeContentToFile(String content, String filePath) {
        try {
            File file = new File(filePath);
            boolean result = createFile(file);
            if (!result) {
                return false;
            } else {
                OutputStream os = new FileOutputStream(file);
                PrintStream ps = new PrintStream(os);
                ps.println(content);
                ps.close();
                os.close();
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
