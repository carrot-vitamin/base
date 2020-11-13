package com.project.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author Shaobo Yin
 * 2018/12/28 10:06 AM
 */
public class FileUtils {

    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    private static ClassLoader classLoader = FileUtils.class.getClassLoader();

    /**
     * 获取resources文件夹下文件的路径 可以是test下的resources文件夹
     *
     * @param fileName e.g. config/application.properties
     * @return String
     */
    public static String getResourceFilePath(String fileName) {
        URL url = classLoader.getResource(fileName);
        assert url != null;
        return url.getPath();
    }

    /**
     * 获取resources文件夹下Properties对象 可以是test下的resources文件夹
     *
     * @param propertiesName properties文件名 e.g. config/application.properties
     * @return Properties
     * @throws IOException IO异常
     */
    public static Properties getProperties(String propertiesName) throws IOException {
        Properties properties;
        InputStream inputStream = null;
        try {
            inputStream = classLoader.getResourceAsStream(propertiesName);
            properties = new Properties();
            properties.load(inputStream);
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
     * @throws IOException IO异常
     */
    public static String getPropertiesValue(String key, String propertiesName) throws IOException {
        Properties properties = getProperties(propertiesName);
        return properties.getProperty(key);
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
     * @throws IOException IOException
     */
    public static File saveFileByInputStream(InputStream inputStream, String localFilePath, String fileName) throws IOException {
        File file = createFile(localFilePath + fileName);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int readLength;
            while ((readLength = inputStream.read(buffer)) > 0) {
                byte[] bytes = new byte[readLength];
                System.arraycopy(buffer, 0, bytes, 0, readLength);
                out.write(bytes);
            }
            out.flush();
        } finally {
            IOUtils.close(out);
        }
        return file;
    }

    /**
     * 创建文件，存在则返回
     * @param filePath 文件路径
     * @return 文件对象
     * @throws IOException IO异常
     */
    public static File createFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            if (file.getParentFile().exists()) {
                file.createNewFile();
            } else {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        }
        return file;
    }

    /**
     * 根据网络地址解析后缀名
     * @param url 网络URL地址 e.g. https://www.baidu.com/img/bd_logo1.png
     * @return 解析后的后缀名  bd_logo1.png
     */
    public static String getSuffixNameByURL(String url) {
        String[] split = url.split("/");
        return split[split.length - 1];
    }

    /**
     * 计算文件大小 单位：字节 (kb)
     * @param base64 base64字符串
     * @return 文件大小 单位：字节 (kb)
     * @throws IOException IO异常
     */
    public static int calcFileProperty(String base64) throws IOException {
        int size;
        assert base64 != null;
        InputStream inputStream = null;
        try {
            byte[] b = Base64.getDecoder().decode(base64);
            inputStream = new ByteArrayInputStream(b);
            size = inputStream.available();
        } finally {
            IOUtils.close(inputStream);
        }
        return size;
    }

    /**
     * 读取文本内容
     * @param filePath 文件路径
     * @return 文本内容
     * @throws FileNotFoundException IO异常
     */
    public static String readTextContent(String filePath) throws FileNotFoundException {
        InputStream inputStream = null;
        Scanner scanner = null;
        try {
            inputStream = new FileInputStream(filePath);
            scanner = new Scanner(inputStream, "UTF-8");
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\n");
            }
            return builder.toString();
        } finally {
            IOUtils.close(scanner);
            IOUtils.close(inputStream);
        }
    }

    /**
     * 将内容写入指定文件，文件不存在则新建（覆盖原内容）
     * @param content 要写入的内容
     * @param filePath 要写入的文件路径
     * @return 写入结果
     * @throws Exception IO异常
     */
    public static boolean writeWithCover(String content, String filePath) throws Exception {
        return writeContent(content, filePath, false);
    }

    /**
     * 将内容写入指定文件，文件不存在则新建（不覆盖原内容）
     * @param content 要写入的内容
     * @param filePath 要写入的文件路径
     * @return 写入结果
     * @throws Exception IO异常
     */
    public static boolean writeWithAppend(String content, String filePath) throws Exception {
        return writeContent(content, filePath, true);
    }

    private static boolean writeContent(String content, String filePath, boolean cover) throws Exception {
        OutputStream os = null;
        PrintStream ps = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                log.info("文件路径【{}】不存在，开始新建", filePath);
                file = createFile(filePath);
            }
            os = new FileOutputStream(file, cover);
            ps = new PrintStream(os);
            ps.println(content);
            return true;
        } finally {
            IOUtils.close(ps);
            IOUtils.close(os);
        }
    }
}
