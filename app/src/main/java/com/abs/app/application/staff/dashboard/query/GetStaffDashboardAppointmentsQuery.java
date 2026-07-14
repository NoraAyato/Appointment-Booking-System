package com.abs.app.application.staff.dashboard.query;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetStaffDashboardAppointmentsQuery {
    private String staffId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;
    private int page;
    private int limit;
}
