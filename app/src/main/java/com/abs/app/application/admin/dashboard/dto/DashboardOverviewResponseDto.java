package com.abs.app.application.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverviewResponseDto {
    private long totalAppointments;
    private long pendingAppointments;
    private long confirmedAppointments;
    private long completedAppointments;
    private long cancelledAppointments;
    private double totalRevenue;
    private long paidInvoices;
    private long newCustomers;
    private long activeServices;
    private long activeStaff;
    private long pendingReviews;
    private double averageRating;
}
