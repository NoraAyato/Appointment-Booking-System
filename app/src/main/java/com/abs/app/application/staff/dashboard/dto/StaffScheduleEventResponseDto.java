package com.abs.app.application.staff.dashboard.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class StaffScheduleEventResponseDto {
    private String type;
    private Long shiftId;
    private Long blockedSlotId;
    private Long appointmentDetailId;
    private String appointmentId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String title;
    private String serviceId;
    private String serviceName;
    private String customerName;
    private String customerPhone;
    private String reason;
}
