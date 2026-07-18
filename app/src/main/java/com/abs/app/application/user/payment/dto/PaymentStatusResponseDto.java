package com.abs.app.application.user.payment.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentStatusResponseDto {
    private String paymentId;
    private String invoiceId;
    private Double amount;
    private String paymentMethod;
    private String status;
    private String orderId;
    private String requestId;
    private LocalDateTime paymentDate;
}
