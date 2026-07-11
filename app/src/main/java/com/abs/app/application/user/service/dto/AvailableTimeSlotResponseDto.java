package com.abs.app.application.user.service.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTimeSlotResponseDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private int availableStaffCount;
}
