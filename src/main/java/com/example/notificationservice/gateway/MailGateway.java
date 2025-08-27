package com.example.notificationservice.gateway;

public interface MailGateway {
    void send(String to, String subject, String body);
}