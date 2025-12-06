package com.tansen.common.service.impl;

import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Value("${mail.username}")
    private String sender;

    private final JavaMailSender mailSender;
    private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendEmail(SendEmailRequest request){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(sender);
            helper.setTo(request.getRecipient());
            helper.setSubject(request.getSubject());
            helper.setText(request.getMessage(),true);
            mailSender.send(mimeMessage);
            LOG.info("Email successfully sent to {}", request.getRecipient());

        } catch (MessagingException e){
            throw new RuntimeException("Failed to send email" +e.getMessage());

        }
    }
}
