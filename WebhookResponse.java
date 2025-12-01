package com.bajaj.hiring.dto;

import lombok.Data;

@Data
public class WebhookResponse {
    private String webhookUrl;
    private String accessToken;
}
