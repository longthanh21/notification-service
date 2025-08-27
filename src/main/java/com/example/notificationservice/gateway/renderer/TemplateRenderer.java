package com.example.notificationservice.gateway.renderer;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TemplateRenderer {
    public String render(String template, Map<String,String> params) {
        String out = template;
        if (params != null) {
            for (var e : params.entrySet()) {
                out = out.replace("{" + e.getKey() + "}", e.getValue());
            }
        }
        return out;
    }
}
