package com.abs.app.application.admin.staffshift.command;

import jakarta.validation.constraints.NotBlank;
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
public class UpdateStaffShiftCommand {

    private Long id;

    private String staffId;

    private LocalDate workDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String status;
}
