package com.abs.app.application.staff.staffshift.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class StaffShiftResponseDto {

    private Long id;

    private LocalDate workDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String status;

    private List<String> serviceNames;
}
