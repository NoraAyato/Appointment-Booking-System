package com.abs.app.application.staff.blockedslot.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.abs.app.common.constant.BlockedSlotConstant;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBlockedSlotRequestDto {
    @NotBlank(message = BlockedSlotConstant.REASON_REQUIRED)
    private String reason;
    private LocalDate blockedDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
