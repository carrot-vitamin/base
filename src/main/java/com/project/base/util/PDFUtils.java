package com.project.base.util;

import com.project.base.model.enums.DateFormatEnum;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author yinshaobo at 2021/5/19 15:06
 */
public class PDFUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFUtils.class);

    /**
     * 合并PDF文档
     *
     * @param destDir  合并后存储的目的地文件夹
     * @param pdfPaths 要合并的pdf文件路径集合
     */
    public static void mergePDF(String destDir, List<String> pdfPaths) {
        //pdf合并工具类
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        //合并pdf生成的文件名
        String destinationFileName = DateFormatEnum.yyyyMMddHHmmsss.getDateFormat().format(new Date()) + ".pdf";
        File fileDir = new File(destDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        pdfPaths.forEach(mergePdf::addSource);
        //设置合并生成pdf文件名称
        mergePdf.setDestinationFileName(destDir + File.separator + destinationFileName);
        //合并pdf
        try {
            mergePdf.mergeDocuments();
            LOGGER.info("合并后的PDF文件路径：【{}】", destDir + File.separator + destinationFileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

