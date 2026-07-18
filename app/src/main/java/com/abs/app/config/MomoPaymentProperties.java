package com.abs.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "momo")
public class MomoPaymentProperties {
    private String endpoint;
    private String partnerCode;
    private String accessKey;
    private String secretKey;
    private String redirectUrl;
    private String ipnUrl;
    private String requestType;
}
