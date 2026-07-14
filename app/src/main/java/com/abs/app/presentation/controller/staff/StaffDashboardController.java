package com.abs.app.presentation.controller.staff;

import java.time.LocalDate;
import java.util.List;

import com.abs.app.application.staff.dashboard.dto.StaffDashboardAppointmentResponseDto;
import com.abs.app.application.staff.dashboard.dto.StaffDashboardOverviewResponseDto;
import com.abs.app.application.staff.dashboard.dto.StaffScheduleEventResponseDto;
import com.abs.app.application.staff.dashboard.query.GetStaffDashboardAppointmentsQuery;
import com.abs.app.application.staff.dashboard.query.GetStaffDashboardAppointmentsQueryHandler;
import com.abs.app.application.staff.dashboard.query.GetStaffDashboardOverviewQuery;
import com.abs.app.application.staff.dashboard.query.GetStaffDashboardOverviewQueryHandler;
import com.abs.app.application.staff.dashboard.query.GetStaffScheduleQuery;
import com.abs.app.application.staff.dashboard.query.GetStaffScheduleQueryHandler;
import com.abs.app.common.constant.StaffDashboardConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import com.abs.app.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffDashboardController {
    private final GetStaffScheduleQueryHandler getStaffScheduleQueryHandler;
    private final GetStaffDashboardAppointmentsQueryHandler getStaffDashboardAppointmentsQueryHandler;
    private final GetStaffDashboardOverviewQueryHandler getStaffDashboardOverviewQueryHandler;

    @GetMapping("/schedule")
    public ResponseEntity<ApiResponse<List<StaffScheduleEventResponseDto>>> getSchedule(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        String staffId = SecurityUtils.getCurrentUserId();
        List<StaffScheduleEventResponseDto> schedule = getStaffScheduleQueryHandler.handle(
                new GetStaffScheduleQuery(staffId, fromDate, toDate));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StaffDashboardConstant.GET_SCHEDULE_SUCCESS,
                schedule));
    }

    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<PageResponse<StaffDashboardAppointmentResponseDto>>> getAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        String staffId = SecurityUtils.getCurrentUserId();
        PageResponse<StaffDashboardAppointmentResponseDto> appointments = getStaffDashboardAppointmentsQueryHandler
                .handle(new GetStaffDashboardAppointmentsQuery(staffId, fromDate, toDate, status, page, limit));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StaffDashboardConstant.GET_APPOINTMENTS_SUCCESS,
                appointments));
    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<StaffDashboardOverviewResponseDto>> getOverview(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        String staffId = SecurityUtils.getCurrentUserId();
        StaffDashboardOverviewResponseDto overview = getStaffDashboardOverviewQueryHandler.handle(
                new GetStaffDashboardOverviewQuery(staffId, fromDate, toDate));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StaffDashboardConstant.GET_OVERVIEW_SUCCESS,
                overview));
    }
}
