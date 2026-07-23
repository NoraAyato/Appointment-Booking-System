package com.abs.app.infrastructure.mapper;

import java.time.LocalDateTime;

import com.abs.app.application.user.payment.dto.CreateMomoPaymentResponseDto;
import com.abs.app.application.user.payment.dto.PaymentStatusResponseDto;
import com.abs.app.common.constant.PaymentConstant;
import com.abs.app.common.util.GenerateIdUtil;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.entity.enums.PaymentMethod;
import com.abs.app.domain.entity.enums.PaymentStatus;
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

    public static Payment createPendingMomoPayment(Invoice invoice, long amount) {
        Payment payment = new Payment();
        payment.setId(GenerateIdUtil.GenerateId(
                PaymentConstant.SALT_TAG,
                PaymentConstant.STRING_LIMIT));
        payment.setAmount((double) amount);
        payment.setPaymentMethod(PaymentMethod.MOMO);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setInvoice(invoice);
        payment.setOrderId(payment.getId());
        payment.setRequestId(GenerateIdUtil.GenerateId(
                PaymentConstant.REQUEST_SALT_TAG,
                PaymentConstant.STRING_LIMIT));
        return payment;
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
