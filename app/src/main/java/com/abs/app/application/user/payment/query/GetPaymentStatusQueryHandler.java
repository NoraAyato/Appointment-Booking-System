package com.abs.app.application.user.payment.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.payment.dto.PaymentStatusResponseDto;
import com.abs.app.common.constant.PaymentConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.repository.PaymentRepository;
import com.abs.app.infrastructure.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetPaymentStatusQueryHandler {
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public PaymentStatusResponseDto handle(GetPaymentStatusQuery query) {
        Payment payment = paymentRepository.findByIdAndCustomerId(query.getPaymentId(), query.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(PaymentConstant.PAYMENT_NOT_FOUND));

        return PaymentMapper.toPaymentStatusResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentStatusResponseDto handle(GetLatestPaymentByInvoiceQuery query) {
        Payment payment = paymentRepository.findLatestByInvoiceIdAndCustomerId(query.getInvoiceId(), query.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(PaymentConstant.PAYMENT_NOT_FOUND));

        return PaymentMapper.toPaymentStatusResponse(payment);
    }
}
