package com.project.base.service.impl;

import com.project.base.service.IEmailService;
import com.project.base.util.RSAUtils;
import com.project.base.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @author yinshaobo
 * 2019/4/12 13:35
 */
public abstract class AbstractEmailService implements IEmailService {

    private static Logger log = LoggerFactory.getLogger(AbstractEmailService.class);

    private static final String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJDdXG7Xr5PpddRW/00mwAspNCSjLAZHtuu7gzXtAJayf9MFvu4RA2AKGBp83BY3PmdhXSJ6uDin0QDHpS8lgvC8/i1SD/tJM5YNtNaZ6vaGXoDOQMqJqpvEA6vs81zeUav9V3RD8WAnkWgm/KN1OB6Xh/Xlnrhz6XLhxef1S/aJAgMBAAECgYAV5zJTnA7zCUiEi4bcbnB4/4jfxUAhzvQMXnSvUZ9WKbUD/glpS288NSqBzsEkJsQrs1/2l6GFB3KlcIo8P6q1aU8zOfSsSFL4HYHx+U5P7aflx8+vYR7KeZDPT+j0js9a9kkA4bwguNdz1Fwmry6e8oD5Z9Lya+cboo5BQJ6nAQJBAPpDBfpu34MLCKRQqlsX9i/RzXqVDVY/qwaH1Uo1sxTYH4W+LzQTd126fHcZybGzm9BdgGqXmPk0LneHOTYuW+ECQQCUL66Vw+2UurdNzEAAtZGv6u+Ht2/8GbMQBQAH4N1ITTIa9Hae1ojWcUs658IXFhaaa+bMbNRUMzmtIoBgMy+pAkB8PjoBknm0fQ3VRZbNkp/OLcJtUQJGB2XI4DarmO1HM+SyaTDWEbP4/FQ+bLcNWbXJRCI0yP5Q+e73uFJW670BAkBYDVUyDK/5dlFBWyKUyU7+Nx2JiUhzhlnOJQp1o/oY4jXpqmVp0JSSx9Mp91hvG2Lm80K7KdepCJ82749UmlWxAkBH+Lthrl3m0wj+i3jKRooQaKr7JQXlSoysrC11WN5NhomSPuMvGVu2JsLPxGnTCpGh3k1JB4kgOZTj1H23Gjma";

    private static Properties props = new Properties();

    /**
     * 发件人账户名
     */
    private String senderAddress;

    /**
     * 发件人账户密码（授权码）
     */
    private String senderPassword;

    /**
     * 初始化邮箱构造函数
     * @param senderAddress 163邮箱账户 使用颁发的公钥加密
     * @param senderPassword 邮箱客户端授权码 使用颁发的公钥加密
     */
    public AbstractEmailService(String senderAddress, String senderPassword) {
        if (StringUtils.isBlank(senderAddress) || StringUtils.isBlank(senderPassword)) {
            log.warn("init 163 email error...account={}, pwd={}", senderAddress, senderPassword);
            throw new RuntimeException("init 163 email error, account or password is blank");
        }
        try {
            senderAddress = RSAUtils.decryptWithPrivateKey(senderAddress, PRIVATE_KEY);
            senderPassword = RSAUtils.decryptWithPrivateKey(senderPassword, PRIVATE_KEY);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (!senderAddress.endsWith("@163.com")) {
            log.warn("init 163 email error, not support other type email...account={}", senderAddress);
            throw new RuntimeException("init 163 email error, not support other type email");
        }
        this.senderAddress = senderAddress;
        this.senderPassword = senderPassword;
    }

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
     *
     * @param session session
     * @return MimeMessage
     * @throws java.lang.Exception exception
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
     *
     * @param msg MimeMessage
     * @param content content
     * @param filePath filePath
     * @throws Exception Exception
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
        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
        session.setDebug(true);
        //3、创建邮件的实例对象
        Message msg = this.getMimeMessage(session, this.senderAddress, toEmail, subject, content, filePath);
        //4、根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(this.senderAddress, this.senderPassword);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
//        transport.sendMessage(msg, msg.getAllRecipients());

        //如果只想发送给指定的人，可以如下写法
        transport.sendMessage(msg, new Address[]{new InternetAddress(toEmail)});

        //5、关闭邮件连接
        transport.close();
        log.info("text email send success... ... to={}", toEmail);
    }

}
