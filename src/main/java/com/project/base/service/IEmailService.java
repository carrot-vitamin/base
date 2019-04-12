package com.project.base.service;

/**
 * @ClassName: IEmailService
 * @Description:
 * @author: yinshaobo
 * @date: 2019/4/12 13:34
 */
public interface IEmailService {

    void sendTextEmail(String toEmail, String subject, String content) throws Exception;

    void sendAttachmentEmail(String toEmail, String subject, String content, String filePath) throws Exception;
}
