package com.abs.app.application.staff.blockedslot.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.abs.app.common.constant.BlockedSlotConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBlockedSlotRequestDto {
    @NotBlank(message = BlockedSlotConstant.REASON_REQUIRED)
    private String reason;
    @NotNull(message = BlockedSlotConstant.BLOCKED_DATE_REQUIRED)
    private LocalDate blockedDate;
    @NotNull(message = BlockedSlotConstant.START_TIME_REQUIRED)
    private LocalTime startTime;
    @NotNull(message = BlockedSlotConstant.END_TIME_REQUIRED)
    private LocalTime endTime;
}
