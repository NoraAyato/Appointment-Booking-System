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
public class CreateStaffShiftCommand {

    private String userId;

    private LocalDate workDate;

    private LocalTime startTime;

    private LocalTime endTime;
}
