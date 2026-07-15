package com.abs.app.application.user.invoice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceResponseDto {
    private String invoiceId;
    private String totalPrice;
    private LocalDateTime createdAt;
    private String invoiceStatus;
    private String serviceName;
    private String categoryName;
    private String categoryColorTag;
    private String serviceDescription;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private String appointmentStatus;
    private LocalTime endTime;
    private int duration;
    private String note;
    private String serviceImage;
    private String staffName;
    private String staffImage;
    private List<String> staffSpecializations;
}
