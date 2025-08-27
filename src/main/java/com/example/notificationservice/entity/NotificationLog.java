package com.example.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="notification_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    private String recipient;
    private String templateCode;
    private String subject;

    @Column(columnDefinition="TEXT")
    private String content;

    private String channel; // EMAIL...
    private Integer status; // 0=PENDING,1=SENT,2=FAILED,3=READ
    private String errorMessage;
    private String dedupeKey;
    private LocalDateTime sentAt;

    @Column(name="created_at")
    private LocalDateTime createdAt;
}
