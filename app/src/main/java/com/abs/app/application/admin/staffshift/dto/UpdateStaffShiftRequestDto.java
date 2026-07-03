package com.abs.app.application.admin.staffshift.dto;

import com.abs.app.common.constant.StaffShiftConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UpdateStaffShiftRequestDto {

    @NotBlank(message = StaffShiftConstant.VALID_STAFF_ID)
    private String staffId;

    @NotNull(message = StaffShiftConstant.VALID_WORK_DATE)
    private LocalDate workDate;

    @NotNull(message = StaffShiftConstant.VALID_START_TIME)
    private LocalTime startTime;

    @NotNull(message = StaffShiftConstant.VALID_END_TIME)
    private LocalTime endTime;

    @NotBlank(message = StaffShiftConstant.VALID_STATUS)
    private String status;
}
