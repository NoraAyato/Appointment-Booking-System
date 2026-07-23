package com.abs.app.application.admin.dashboard.query;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDateRangeQuery {
    private LocalDate fromDate;
    private LocalDate toDate;
}
