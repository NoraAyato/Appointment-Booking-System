package com.abs.app.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.repository.InvoiceRepository;
import com.abs.app.infrastructure.persistence.jpa.InvoiceJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final InvoiceJpaRepository invoiceJpaRepository;

    @Override
    public Optional<Invoice> findByIdAndCustomerId(String invoiceId, String customerId) {
        return invoiceJpaRepository.findByIdAndCustomerId(invoiceId, customerId);
    }

    @Override
    public double sumAmountByStatusAndCreatedAtBetween(
            InvoiceStatus status,
            LocalDateTime startAt,
            LocalDateTime endAt) {
        Double total = invoiceJpaRepository.sumAmountByStatusAndCreatedAtBetween(status, startAt, endAt);
        return total != null ? total : 0D;
    }

    @Override
    public long countByStatusAndCreatedAtBetween(
            InvoiceStatus status,
            LocalDateTime startAt,
            LocalDateTime endAt) {
        return invoiceJpaRepository.countByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                status,
                startAt,
                endAt);
    }

    @Override
    public List<Object[]> getDailyRevenue(
            InvoiceStatus status,
            LocalDateTime startAt,
            LocalDateTime endAt) {
        return invoiceJpaRepository.getDailyRevenue(status.name(), startAt, endAt);
    }
}
