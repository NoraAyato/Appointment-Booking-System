package com.abs.app.domain.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.abs.app.domain.entity.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findByOrderIdAndRequestId(String orderId, String requestId);

    Optional<Payment> findByIdAndCustomerId(String paymentId, String customerId);

    Optional<Payment> findLatestByInvoiceIdAndCustomerId(String invoiceId, String customerId);

    Map<String, Payment> findLatestByInvoiceIds(List<String> invoiceIds);

    int failPendingPaymentsByInvoiceIds(List<String> invoiceIds);
}
