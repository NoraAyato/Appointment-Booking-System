package com.abs.app.infrastructure.payment.momo.dto;

import lombok.Data;

@Data
public class MomoCreatePaymentRequest {
    private String partnerCode;
    private String partnerName;
    private String storeId;
    private String requestId;
    private String amount;
    private String orderId;
    private String orderInfo;
    private String redirectUrl;
    private String ipnUrl;
    private String requestType;
    private String extraData;
    private String lang;
    private Boolean autoCapture;
    private String signature;
}
