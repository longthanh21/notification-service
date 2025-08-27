package com.example.notificationservice.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class SendRequest {

    private String recipient;
    private String templateCode;
    private Map<String,String> params;
    private String dedupeKey; // optional
}
