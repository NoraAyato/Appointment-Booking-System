package com.abs.app.application.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStaffPerformanceResponseDto {
    private String staffId;
    private String staffName;
    private long completedAppointments;
    private long cancelledAppointments;
    private double averageRating;
    private double totalWorkingHours;
}
