package com.example.notificationservice.controller;

import com.example.notificationservice.dto.request.SendRequest;
import com.example.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;


    @PostMapping("/send")
    public ResponseEntity<Map<String,Object>> send(@RequestBody SendRequest req) {
        Long id = service.sendByTemplate(req);
        return ResponseEntity.ok(Map.of("id", id, "status", "OK"));
    }
}
