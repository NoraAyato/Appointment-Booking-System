package com.abs.app.application.staff.dashboard.query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;

@Getter
public class StaffDashboardDateRange {
    private final LocalDate fromDate;
    private final LocalDate toDate;

    private StaffDashboardDateRange(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public static StaffDashboardDateRange of(LocalDate requestedFromDate, LocalDate requestedToDate) {
        LocalDate normalizedFromDate = requestedFromDate != null ? requestedFromDate : LocalDate.now();
        LocalDate normalizedToDate = requestedToDate != null ? requestedToDate : normalizedFromDate.plusDays(6);

        if (normalizedFromDate.isAfter(normalizedToDate)) {
            return new StaffDashboardDateRange(normalizedToDate, normalizedFromDate);
        }

        return new StaffDashboardDateRange(normalizedFromDate, normalizedToDate);
    }

    public LocalDateTime startAt() {
        return fromDate.atStartOfDay();
    }

    public LocalDateTime endExclusive() {
        return toDate.plusDays(1).atStartOfDay();
    }

    public LocalTime eventStartTimeOrMin(LocalTime startTime) {
        return startTime != null ? startTime : LocalTime.MIN;
    }
}
