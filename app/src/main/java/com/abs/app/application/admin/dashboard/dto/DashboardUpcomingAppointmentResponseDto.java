package com.abs.app.application.admin.dashboard.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardUpcomingAppointmentResponseDto {
    private String appointmentId;
    private String customerName;
    private String serviceName;
    private String staffName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
