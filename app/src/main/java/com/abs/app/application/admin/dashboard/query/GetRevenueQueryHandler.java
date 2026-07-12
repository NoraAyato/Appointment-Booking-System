package com.abs.app.application.admin.dashboard.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.admin.dashboard.dto.DashboardRevenueResponseDto;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.repository.InvoiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetRevenueQueryHandler {
    private final InvoiceRepository invoiceRepository;

    @Transactional(readOnly = true)
    public List<DashboardRevenueResponseDto> handle(DashboardDateRangeQuery query) {
        DashboardDateRange range = DashboardDateRange.of(query.getFromDate(), query.getToDate());

        return invoiceRepository.getDailyRevenue(
                InvoiceStatus.PAID,
                range.getStartAt(),
                range.getEndAtExclusive())
                .stream()
                .map(row -> new DashboardRevenueResponseDto(
                        DashboardRowMapper.localDateValue(row[0]),
                        DashboardRowMapper.doubleValue(row[1]),
                        DashboardRowMapper.longValue(row[2])))
                .toList();
    }
}
