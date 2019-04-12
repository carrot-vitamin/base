package com.project.base.service.impl;

import javax.mail.internet.MimeMessage;

/**
 * @ClassName: TextEmailServiceImpl
 * @Description: 仅包含文本的简单邮件
 * @author: yinshaobo
 * @date: 2019/4/12 14:31
 */
public class TextEmailServiceImpl extends AbstractEmailService {

    @Override
    public void operate(MimeMessage msg, String content, String filePath) throws Exception {
        //设置邮件正文
        msg.setContent(content, "text/html;charset=UTF-8");
    }
}
