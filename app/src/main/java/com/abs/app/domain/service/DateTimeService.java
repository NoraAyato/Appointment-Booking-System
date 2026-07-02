package com.abs.app.domain.service;

import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

@Service
public class DateTimeService {
    public boolean isValidTimeRange(LocalTime startTime, LocalTime endTime) {
        return startTime.isBefore(endTime);
    }

    public boolean isValidDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        return !date.isBefore(today);
    }

    public boolean isOverlapping(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
        return startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1);
    }

    public boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate);
    }

    public boolean isOverlapping(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2,
            LocalDate endDate2) {
        return !startDate1.isAfter(endDate2) && !startDate2.isAfter(endDate1);
    }

    public boolean isOverlapping(LocalDateTime startTime1, LocalDateTime endTime1,
            LocalDateTime startTime2,
            LocalDateTime endTime2) {
        return !startTime1.isAfter(endTime2) && !startTime2.isAfter(endTime1);
    }
}
