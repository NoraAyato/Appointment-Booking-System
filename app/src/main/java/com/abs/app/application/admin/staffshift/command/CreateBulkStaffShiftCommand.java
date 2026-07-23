package com.abs.app.application.admin.staffshift.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBulkStaffShiftCommand {
    private LocalDate startDate;

    private LocalDate endDate;

    private List<Integer> workingDays;

    private LocalTime startTime;

    private LocalTime endTime;
}