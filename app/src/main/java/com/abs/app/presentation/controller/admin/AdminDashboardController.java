package com.abs.app.presentation.controller.admin;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.admin.dashboard.dto.DashboardAlertsResponseDto;
import com.abs.app.application.admin.dashboard.dto.DashboardDailyAppointmentsResponseDto;
import com.abs.app.application.admin.dashboard.dto.DashboardOverviewResponseDto;
import com.abs.app.application.admin.dashboard.dto.DashboardRevenueResponseDto;
import com.abs.app.application.admin.dashboard.dto.DashboardStaffPerformanceResponseDto;
import com.abs.app.application.admin.dashboard.dto.DashboardStatusCountResponseDto;
import com.abs.app.application.admin.dashboard.dto.DashboardTopServiceResponseDto;
import com.abs.app.application.admin.dashboard.dto.DashboardUpcomingAppointmentResponseDto;
import com.abs.app.application.admin.dashboard.query.DashboardDateRangeQuery;
import com.abs.app.application.admin.dashboard.query.DashboardLimitQuery;
import com.abs.app.application.admin.dashboard.query.GetAppointmentStatusSummaryQueryHandler;
import com.abs.app.application.admin.dashboard.query.GetDailyAppointmentsQueryHandler;
import com.abs.app.application.admin.dashboard.query.GetDashboardAlertsQueryHandler;
import com.abs.app.application.admin.dashboard.query.GetDashboardOverviewQueryHandler;
import com.abs.app.application.admin.dashboard.query.GetRevenueQueryHandler;
import com.abs.app.application.admin.dashboard.query.GetStaffPerformanceQueryHandler;
import com.abs.app.application.admin.dashboard.query.GetTopServicesQueryHandler;
import com.abs.app.application.admin.dashboard.query.GetUpcomingAppointmentsQueryHandler;
import com.abs.app.common.constant.DashboardConstant;
import com.abs.app.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {
    private final GetDashboardOverviewQueryHandler getDashboardOverviewQueryHandler;
    private final GetAppointmentStatusSummaryQueryHandler getAppointmentStatusSummaryQueryHandler;
    private final GetDailyAppointmentsQueryHandler getDailyAppointmentsQueryHandler;
    private final GetRevenueQueryHandler getRevenueQueryHandler;
    private final GetTopServicesQueryHandler getTopServicesQueryHandler;
    private final GetStaffPerformanceQueryHandler getStaffPerformanceQueryHandler;
    private final GetDashboardAlertsQueryHandler getDashboardAlertsQueryHandler;
    private final GetUpcomingAppointmentsQueryHandler getUpcomingAppointmentsQueryHandler;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<DashboardOverviewResponseDto>> getOverview(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        DashboardOverviewResponseDto overview = getDashboardOverviewQueryHandler
                .handle(new DashboardDateRangeQuery(fromDate, toDate));
        return ResponseEntity.ok(new ApiResponse<>(true, DashboardConstant.GET_OVERVIEW_SUCCESS, overview));
    }

    @GetMapping("/appointments/status-summary")
    public ResponseEntity<ApiResponse<List<DashboardStatusCountResponseDto>>> getAppointmentStatusSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<DashboardStatusCountResponseDto> summary = getAppointmentStatusSummaryQueryHandler
                .handle(new DashboardDateRangeQuery(fromDate, toDate));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                DashboardConstant.GET_APPOINTMENT_STATUS_SUMMARY_SUCCESS,
                summary));
    }

    @GetMapping("/appointments/daily")
    public ResponseEntity<ApiResponse<List<DashboardDailyAppointmentsResponseDto>>> getDailyAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<DashboardDailyAppointmentsResponseDto> appointments = getDailyAppointmentsQueryHandler
                .handle(new DashboardDateRangeQuery(fromDate, toDate));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                DashboardConstant.GET_DAILY_APPOINTMENTS_SUCCESS,
                appointments));
    }

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<List<DashboardRevenueResponseDto>>> getRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<DashboardRevenueResponseDto> revenue = getRevenueQueryHandler
                .handle(new DashboardDateRangeQuery(fromDate, toDate));
        return ResponseEntity.ok(new ApiResponse<>(true, DashboardConstant.GET_REVENUE_SUCCESS, revenue));
    }

    @GetMapping("/services/top")
    public ResponseEntity<ApiResponse<List<DashboardTopServiceResponseDto>>> getTopServices(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "5") int limit) {
        List<DashboardTopServiceResponseDto> services = getTopServicesQueryHandler
                .handle(new DashboardLimitQuery(fromDate, toDate, limit));
        return ResponseEntity.ok(new ApiResponse<>(true, DashboardConstant.GET_TOP_SERVICES_SUCCESS, services));
    }

    @GetMapping("/staff/performance")
    public ResponseEntity<ApiResponse<List<DashboardStaffPerformanceResponseDto>>> getStaffPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "10") int limit) {
        List<DashboardStaffPerformanceResponseDto> staff = getStaffPerformanceQueryHandler
                .handle(new DashboardLimitQuery(fromDate, toDate, limit));
        return ResponseEntity.ok(new ApiResponse<>(true, DashboardConstant.GET_STAFF_PERFORMANCE_SUCCESS, staff));
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<DashboardAlertsResponseDto>> getAlerts() {
        DashboardAlertsResponseDto alerts = getDashboardAlertsQueryHandler.handle();
        return ResponseEntity.ok(new ApiResponse<>(true, DashboardConstant.GET_ALERTS_SUCCESS, alerts));
    }

    @GetMapping("/appointments/upcoming")
    public ResponseEntity<ApiResponse<List<DashboardUpcomingAppointmentResponseDto>>> getUpcomingAppointments(
            @RequestParam(defaultValue = "10") int limit) {
        List<DashboardUpcomingAppointmentResponseDto> appointments = getUpcomingAppointmentsQueryHandler.handle(limit);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                DashboardConstant.GET_UPCOMING_APPOINTMENTS_SUCCESS,
                appointments));
    }
}
