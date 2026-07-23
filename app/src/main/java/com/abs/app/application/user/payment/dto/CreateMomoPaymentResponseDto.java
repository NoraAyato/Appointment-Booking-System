package com.abs.app.application.user.payment.dto;

import lombok.Data;

@Data
public class CreateMomoPaymentResponseDto {
    private String paymentId;
    private String invoiceId;
    private String orderId;
    private String requestId;
    private String payUrl;
    private String deeplink;
    private String qrCodeUrl;
}
