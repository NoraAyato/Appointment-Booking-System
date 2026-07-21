package com.abs.app.application.staff.staffshift.dto;

import com.abs.app.common.constant.StaffShiftConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "CreateBulkStaffShiftRequestDto")
public class CreateBulkStaffShiftRequestDto {

    @NotNull(message = StaffShiftConstant.VALID_WORK_DATE)
    private LocalDate startDate;

    @NotNull(message = StaffShiftConstant.VALID_WORK_DATE)
    private LocalDate endDate;

    @NotNull(message = StaffShiftConstant.VALID_WORK_DATE)
    private List<Integer> workingDays;

    @NotNull(message = StaffShiftConstant.VALID_START_TIME)
    private LocalTime startTime;

    @NotNull(message = StaffShiftConstant.VALID_END_TIME)
    private LocalTime endTime;
}
