package com.project.base.service.impl;

import com.project.base.service.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @ClassName: AbstractEmailService
 * @Description:
 * @author: yinshaobo
 * @date: 2019/4/12 13:35
 */
@Slf4j
public abstract class AbstractEmailService implements IEmailService {

    /**
     * 默认收件人地址
     */
    private static String defaultRecipientAddress = "XXX@163.com";
    /**
     * 发件人地址
     */
    private static String senderAddress = "XXX@163.com";
    /**
     * 发件人账户名
     */
    private static String senderAccount = "XXX";
    /**
     * 发件人账户密码（授权码）
     */
    private static String senderPassword = "******";

    private static Properties props = new Properties();

    static {
        //1、连接邮件服务器的参数配置
        //设置用户的认证方式
        props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.163.com");
    }

    /**
     * 获得创建一封邮件的实例对象
     * @param session
     * @return
     * @throws Exception
     */
    private MimeMessage getMimeMessage(Session session, String from, String to, String subject, String content, String filePath) throws Exception {
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(from));
        //设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
        //MimeMessage.RecipientType.TO:发送 CC：抄送 BCC：密送
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
        //设置邮件主题
        msg.setSubject(subject);

        //在这里对纯文本或附件做特殊处理逻辑
        this.operate(msg, content, filePath);

        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());
        return msg;
    }

    /**
     * 纯文本或带有附件的特殊处理逻辑方法
     * @param msg
     * @param content
     * @param filePath
     * @throws Exception
     */
    public abstract void operate(MimeMessage msg, String content, String filePath) throws Exception;

    @Override
    public void sendTextEmail(String toEmail, String subject, String content) throws Exception {
        this.sendEmail(toEmail, subject, content, null);
    }

    @Override
    public void sendAttachmentEmail(String toEmail, String subject, String content, String filePath) throws Exception {
        this.sendEmail(toEmail, subject, content, filePath);
    }

    private void sendEmail(String toEmail, String subject, String content, String filePath) throws Exception {
        toEmail = StringUtils.isNoneBlank(toEmail) ? toEmail : defaultRecipientAddress;
        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
        session.setDebug(true);
        //3、创建邮件的实例对象
        Message msg = this.getMimeMessage(session,senderAddress, toEmail, subject, content, filePath);
        //4、根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
//        transport.sendMessage(msg, msg.getAllRecipients());

        //如果只想发送给指定的人，可以如下写法
        transport.sendMessage(msg, new Address[]{new InternetAddress(toEmail)});

        //5、关闭邮件连接
        transport.close();
        log.info("text email send success... ... to={}", toEmail);
    }
}
