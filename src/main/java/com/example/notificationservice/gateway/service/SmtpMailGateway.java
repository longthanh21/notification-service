package com.example.notificationservice.gateway.service;

import com.example.notificationservice.gateway.MailGateway;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmtpMailGateway implements MailGateway {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    //    @Override
//    public void send(String to, String subject, String body) {
//        var msg = new SimpleMailMessage();
//        msg.setFrom(from);
//        msg.setTo(to);
//        msg.setSubject(subject);
//        msg.setText(body);
//        mailSender.send(msg);
//    }

    @Override
    public void send(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // <--- true để bật HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi khi gửi mail HTML", e);
        }
    }
}