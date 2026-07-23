package com.abs.app.application.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardAlertsResponseDto {
    private long pendingStaffShifts;
    private long pendingBlockedSlots;
    private long pendingReviews;
    private long servicesWithoutStaff;
    private long staffWithoutShiftToday;
}
