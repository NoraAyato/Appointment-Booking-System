package com.abs.app.application.admin.dashboard.query;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
class DashboardDateRange {
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final LocalDateTime startAt;
    private final LocalDateTime endAtExclusive;

    private DashboardDateRange(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.startAt = fromDate.atStartOfDay();
        this.endAtExclusive = toDate.plusDays(1).atStartOfDay();
    }

    static DashboardDateRange of(LocalDate fromDate, LocalDate toDate) {
        LocalDate today = LocalDate.now();
        LocalDate normalizedToDate = toDate != null ? toDate : today;
        LocalDate normalizedFromDate = fromDate != null ? fromDate : normalizedToDate.withDayOfMonth(1);

        if (normalizedFromDate.isAfter(normalizedToDate)) {
            return new DashboardDateRange(normalizedToDate, normalizedFromDate);
        }

        return new DashboardDateRange(normalizedFromDate, normalizedToDate);
    }
}
