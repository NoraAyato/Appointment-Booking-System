package com.abs.app.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.enums.InvoiceStatus;

public interface InvoiceRepository {
    Optional<Invoice> findByIdAndCustomerId(String invoiceId, String customerId);

    Invoice save(Invoice invoice);

    List<String> findExpiredUnpaidInvoiceIds(
            InvoiceStatus unpaidStatus,
            LocalDateTime expiredBefore,
            int limit);

    int cancelUnpaidInvoicesByIds(
            List<String> invoiceIds,
            InvoiceStatus unpaidStatus,
            InvoiceStatus cancelledStatus);

    double sumAmountByStatusAndCreatedAtBetween(
            InvoiceStatus status,
            LocalDateTime startAt,
            LocalDateTime endAt);

    long countByStatusAndCreatedAtBetween(
            InvoiceStatus status,
            LocalDateTime startAt,
            LocalDateTime endAt);

    List<Object[]> getDailyRevenue(
            InvoiceStatus status,
            LocalDateTime startAt,
            LocalDateTime endAt);
}
