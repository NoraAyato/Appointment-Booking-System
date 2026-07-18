package com.abs.app.infrastructure.mapper;

import com.abs.app.application.user.payment.dto.CreateMomoPaymentResponseDto;
import com.abs.app.application.user.payment.dto.PaymentStatusResponseDto;
import com.abs.app.domain.entity.Payment;
import com.abs.app.infrastructure.payment.momo.dto.MomoCreatePaymentResponse;

public class PaymentMapper {
    private PaymentMapper() {
    }

    public static CreateMomoPaymentResponseDto toCreateMomoPaymentResponse(
            Payment payment,
            MomoCreatePaymentResponse momoResponse) {
        CreateMomoPaymentResponseDto dto = new CreateMomoPaymentResponseDto();
        dto.setPaymentId(payment.getId());
        dto.setInvoiceId(payment.getInvoice().getId());
        dto.setOrderId(payment.getOrderId());
        dto.setRequestId(payment.getRequestId());
        dto.setPayUrl(momoResponse.getPayUrl());
        dto.setDeeplink(momoResponse.getDeeplink());
        dto.setQrCodeUrl(momoResponse.getQrCodeUrl());
        return dto;
    }

    public static PaymentStatusResponseDto toPaymentStatusResponse(Payment payment) {
        PaymentStatusResponseDto dto = new PaymentStatusResponseDto();
        dto.setPaymentId(payment.getId());
        dto.setInvoiceId(payment.getInvoice() != null ? payment.getInvoice().getId() : null);
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null);
        dto.setStatus(payment.getStatus() != null ? payment.getStatus().name() : null);
        dto.setOrderId(payment.getOrderId());
        dto.setRequestId(payment.getRequestId());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }
}
