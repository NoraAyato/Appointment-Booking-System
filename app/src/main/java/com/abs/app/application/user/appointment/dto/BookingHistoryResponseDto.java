package com.abs.app.application.user.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class BookingHistoryResponseDto {
    private Long appointmentDetailId;
    private String appointmentId;
    private String serviceId;
    private String serviceName;
    private String serviceImage;
    private String categoryName;
    private String categoryColorTag;
    private String staffId;
    private String staffName;
    private String staffAvatar;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int durationMinutes;
    private int quantity;
    private String appointmentStatus;
    private String note;
    private String invoiceId;
    private Double invoiceAmount;
    private String invoiceStatus;
    private String promotionCode;
    private String paymentStatus;
    private String paymentMethod;
    private boolean reviewed;
}
