package com.abs.app.application.staff.appointment.query;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetStaffAppointmentListQuery {
    private String staffId;
    private String keyword;
    private LocalDate date;
    private String status;
    private int page;
    private int limit;
}
