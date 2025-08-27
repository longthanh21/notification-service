package com.example.notificationservice.controller;

import com.example.notificationservice.service.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/test")
    public String sendMail() {
        mailService.sendTestMail("long4@yopmail.com");
        return "Đã gửi mail!";
    }
}

