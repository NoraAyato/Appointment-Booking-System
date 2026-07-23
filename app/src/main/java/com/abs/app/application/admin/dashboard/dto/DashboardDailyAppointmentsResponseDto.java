package com.abs.app.application.admin.dashboard.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDailyAppointmentsResponseDto {
    private LocalDate date;
    private long total;
    private long completed;
    private long cancelled;
}
