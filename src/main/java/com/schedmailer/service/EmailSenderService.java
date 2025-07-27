package com.schedmailer.service;

import com.schedmailer.domain.entity.ScheduledEmail;
import com.schedmailer.domain.entity.SmtpConfig;
import com.schedmailer.util.EncryptionUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
public class EmailSenderService {
    @Value("${encryption.key}")
    private String encryptionKey;

    public void send(ScheduledEmail email) throws MessagingException {
        SmtpConfig smtp = email.getSmtpConfig();

        String password = EncryptionUtil.decrypt(smtp.getPassword(), encryptionKey);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtp.getHost());
        mailSender.setPort(smtp.getPort());
        mailSender.setUsername(smtp.getUsername());
        mailSender.setPassword(password);

        mailSender.setJavaMailProperties(buildMailProperties(smtp.isUseTls(), smtp.isUseSsl()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        helper.setTo(email.getToEmail());
        helper.setFrom(smtp.getUsername());
        helper.setSubject(email.getSubject());
        helper.setText(email.getBody(), false); // Set true if HTML body

        mailSender.send(message);

        log.info("Email sent to {}", email.getToEmail());
    }

    private Properties buildMailProperties(boolean useTls, boolean useSsl) {
        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");

        if (useTls) props.put("mail.smtp.starttls.enable", "true");
        if (useSsl) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", "*");
        }

        props.put("mail.debug", "false");
        return props;
    }
}
