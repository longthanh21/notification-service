package com.example.notificationservice.service;

import com.example.notificationservice.dto.request.SendRequest;
import com.example.notificationservice.entity.NotificationLog;
import com.example.notificationservice.gateway.MailGateway;
import com.example.notificationservice.gateway.renderer.TemplateRenderer;
import com.example.notificationservice.repository.NotificationLogRepository;
import com.example.notificationservice.repository.NotificationTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationTemplateRepository templateRepo;
    private final NotificationLogRepository logRepo;
    private final TemplateRenderer renderer;
    private final MailGateway mailGateway;

    @Transactional
    public Long sendByTemplate(SendRequest req) {
        // dedupe optional: chặn gửi trùng trong 5 phút
        if (req.getDedupeKey() != null) {
            var dup = logRepo.findFirstByDedupeKeyAndCreatedAtAfter(req.getDedupeKey(), LocalDateTime.now().minusMinutes(5));
            if (dup.isPresent()) return dup.get().getId();
        }

        var tpl = templateRepo.findByCode(req.getTemplateCode())
                .orElseThrow(() -> new IllegalArgumentException("Template không tồn tại: " + req.getTemplateCode()));

        var subject = renderer.render(tpl.getSubject(), req.getParams());
        var content = renderer.render(tpl.getContent(), req.getParams());

        var log = logRepo.save(NotificationLog.builder()
                .recipient(req.getRecipient())
                .templateCode(req.getTemplateCode())
                .subject(subject)
                .content(content)
                .channel(tpl.getChannel())
                .status(0) // PENDING
                .dedupeKey(req.getDedupeKey())
                .build());

        try {
            if (!"EMAIL".equalsIgnoreCase(tpl.getChannel()))
                throw new UnsupportedOperationException("Channel chưa hỗ trợ: " + tpl.getChannel());

            mailGateway.send(req.getRecipient(), subject, content);
            log.setStatus(1);            // SENT
            log.setSentAt(LocalDateTime.now());
            logRepo.save(log);
        } catch (Exception ex) {
            log.setStatus(2);            // FAILED
            log.setErrorMessage(ex.getMessage());
            logRepo.save(log);
            throw ex;
        }
        return log.getId();
    }
}
