package com.project.base.service;

/**
 * @author yinshaobo
 * 2019/4/12 13:34
 */
public interface IEmailService {

    void sendTextEmail(String toEmail, String subject, String content) throws Exception;

    void sendAttachmentEmail(String toEmail, String subject, String content, String filePath) throws Exception;
}
