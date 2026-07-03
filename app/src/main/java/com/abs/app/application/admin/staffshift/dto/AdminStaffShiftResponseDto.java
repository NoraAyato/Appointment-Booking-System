package com.abs.app.application.admin.staffshift.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AdminStaffShiftResponseDto {
    private Long id;

    private LocalDate workDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String status;

    private String staffName;

    private String staffAvatar;

    private List<String> serviceNames;
}
