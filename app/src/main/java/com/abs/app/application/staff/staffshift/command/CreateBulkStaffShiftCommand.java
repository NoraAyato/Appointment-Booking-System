package com.abs.app.application.staff.staffshift.command;

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

    private String userId;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<Integer> workingDays;

    private LocalTime startTime;

    private LocalTime endTime;
}
