package com.project.base.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * @author za-yinshaobo at 2020/5/27 22:12
 */
public class SystemUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUtils.class);

    /**
     * 将文字复制到windows剪切板（可直接“粘贴”）
     * @param text 要复制的内容
     */
    public static void copyTextToWinClipBoard(String text) {
        if (StringUtils.isNotBlank(text)) {
            StringSelection stringSelection = new StringSelection(text);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
            LOGGER.info("【{}】已复制到剪切板！", text);
        } else {
            LOGGER.warn("要复制的内容为空！");
        }
    }
}
