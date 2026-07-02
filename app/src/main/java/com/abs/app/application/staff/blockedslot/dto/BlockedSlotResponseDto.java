package com.abs.app.application.staff.blockedslot.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import lombok.Data;

@Data
public class BlockedSlotResponseDto {
    private Long id;
    private String reason;
    private String status;
    private LocalDate blockedDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
