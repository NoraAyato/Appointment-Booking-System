package com.abs.app.application.staff.staffshift.dto;

import com.abs.app.common.constant.StaffShiftConstant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "StaffCreateStaffShiftRequestDto")
public class CreateStaffShiftRequestDto {

    @NotNull(message = StaffShiftConstant.VALID_WORK_DATE)
    private LocalDate workDate;

    @NotNull(message = StaffShiftConstant.VALID_START_TIME)
    private LocalTime startTime;

    @NotNull(message = StaffShiftConstant.VALID_END_TIME)
    private LocalTime endTime;
}
