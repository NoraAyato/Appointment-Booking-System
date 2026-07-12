package com.abs.app.application.admin.dashboard.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRevenueResponseDto {
    private LocalDate date;
    private double revenue;
    private long invoiceCount;
}
