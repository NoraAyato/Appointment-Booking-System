package com.abs.app.application.staff.dashboard.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StaffDashboardAppointmentResponseDto {
    private Long appointmentDetailId;
    private String appointmentId;
    private String serviceId;
    private String serviceName;
    private String customerName;
    private String customerPhone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int quantity;
    private String status;
    private String note;
}
