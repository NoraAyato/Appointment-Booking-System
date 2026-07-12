package com.abs.app.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.abs.app.domain.entity.enums.InvoiceStatus;

public interface InvoiceRepository {
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
