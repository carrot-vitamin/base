package com.project.base.service.impl;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * @author yinshaobo
 * 2019/4/12 14:37
 */
public class WithAttachmentEmailServiceImpl extends AbstractEmailService {

    public WithAttachmentEmailServiceImpl(String senderAddress, String senderPassword) {
        super(senderAddress, senderPassword);
    }

    @Override
    public void operate(MimeMessage msg, String content, String filePath) throws Exception {
        // 6. 创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        text.setContent(content, "text/html;charset=UTF-8");
        // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
        MimeMultipart mmTextImage = new MimeMultipart();
        mmTextImage.addBodyPart(text);

        // 8. 将 文本+图片 的混合"节点"封装成一个普通"节点"
        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
        // 上面的 mailTestPic 并非 BodyPart, 所有要把 mmTextImage 封装成一个 BodyPart
        MimeBodyPart textImage = new MimeBodyPart();
        textImage.setContent(mmTextImage);

        // 9. 创建附件"节点"
        MimeBodyPart attachment = new MimeBodyPart();
        // 读取本地文件
        DataHandler dh2 = new DataHandler(new FileDataSource(filePath));
        // 将附件数据添加到"节点"
        attachment.setDataHandler(dh2);
        // 设置附件的文件名（需要编码）
        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));

        // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(textImage);
        // 如果有多个附件，可以创建多个多次添加
        mm.addBodyPart(attachment);
        // 混合关系
        mm.setSubType("mixed");

        msg.setContent(mm);
    }
}
