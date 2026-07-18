package com.abs.app.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.repository.PaymentRepository;
import com.abs.app.infrastructure.persistence.jpa.PaymentJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByOrderIdAndRequestId(String orderId, String requestId) {
        return paymentJpaRepository.findByOrderIdAndRequestId(orderId, requestId);
    }

    @Override
    public Optional<Payment> findByIdAndCustomerId(String paymentId, String customerId) {
        return paymentJpaRepository.findByIdAndCustomerId(paymentId, customerId);
    }

    @Override
    public Optional<Payment> findLatestByInvoiceIdAndCustomerId(String invoiceId, String customerId) {
        return paymentJpaRepository.findByInvoiceIdAndCustomerIdOrderByPaymentDateDesc(invoiceId, customerId)
                .stream()
                .findFirst();
    }
}
