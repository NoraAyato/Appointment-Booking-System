package com.abs.app.application.admin.blockedslot.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBlockedSlotResponseDto {
    private Long id;
    private String reason;
    private String status;
    private LocalDate blockedDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String staffName;
    private String avatarUrl;
}
