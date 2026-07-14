package com.abs.app.application.staff.dashboard.query;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetStaffDashboardOverviewQuery {
    private String staffId;
    private LocalDate fromDate;
    private LocalDate toDate;
}
