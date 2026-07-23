package com.abs.app.infrastructure.persistence.adapter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.entity.enums.PaymentStatus;
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

    @Override
    public Map<String, Payment> findLatestByInvoiceIds(List<String> invoiceIds) {
        if (invoiceIds == null || invoiceIds.isEmpty()) {
            return Map.of();
        }

        Map<String, Payment> latestPayments = new LinkedHashMap<>();
        paymentJpaRepository.findByInvoiceIdsOrderByPaymentDateDesc(invoiceIds)
                .forEach(payment -> latestPayments.putIfAbsent(payment.getInvoice().getId(), payment));
        return latestPayments;
    }

    @Override
    public int failPendingPaymentsByInvoiceIds(List<String> invoiceIds) {
        if (invoiceIds == null || invoiceIds.isEmpty()) {
            return 0;
        }
        return paymentJpaRepository.failPendingPaymentsByInvoiceIds(
                invoiceIds,
                PaymentStatus.PENDING,
                PaymentStatus.FAILED);
    }
}
