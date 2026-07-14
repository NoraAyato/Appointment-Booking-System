package com.abs.app.application.staff.dashboard.dto;

import lombok.Data;

@Data
public class StaffDashboardOverviewResponseDto {
    private int totalAppointments;
    private int pendingAppointments;
    private int confirmedAppointments;
    private int completedAppointments;
    private int cancelledAppointments;
    private double totalWorkingHours;
    private int totalBlockedSlots;
}
