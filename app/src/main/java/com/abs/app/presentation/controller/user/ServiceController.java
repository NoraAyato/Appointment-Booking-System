package com.abs.app.presentation.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.service.dto.AvailableTimeSlotResponseDto;
import com.abs.app.application.user.service.dto.StaffServiceResponseDto;
import com.abs.app.application.user.service.dto.UserServiceResponseDto;
import com.abs.app.application.user.service.query.GetAvailableStaffForServiceQuery;
import com.abs.app.application.user.service.query.GetAvailableStaffForServiceQueryHandler;
import com.abs.app.application.user.service.query.GetAvailableTimeSlotsQuery;
import com.abs.app.application.user.service.query.GetAvailableTimeSlotsQueryHandler;
import com.abs.app.application.user.service.query.GetServiceDataQuery;
import com.abs.app.application.user.service.query.GetServiceDataQueryHandler;
import com.abs.app.application.user.service.query.GetTopRatedServicesQueryHandler;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/public/services")
@RequiredArgsConstructor
public class ServiceController {
    private final GetServiceDataQueryHandler getServiceDataQueryHandler;
    private final GetTopRatedServicesQueryHandler getTopRatedServicesQueryHandler;
    private final GetAvailableStaffForServiceQueryHandler getAvailableStaffForServiceQueryHandler;
    private final GetAvailableTimeSlotsQueryHandler getAvailableTimeSlotsQueryHandler;

    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<List<UserServiceResponseDto>>> getTopRatedServices() {
        List<UserServiceResponseDto> services = getTopRatedServicesQueryHandler.handle();
        return ResponseEntity
                .ok(new ApiResponse<>(true, ServiceEntityConstant.GET_TOP_RATED_SERVICES_SUCCESS, services));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<PageResponse<UserServiceResponseDto>>> getServices(
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalTime time,
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        GetServiceDataQuery query = new GetServiceDataQuery(keyWord, categoryId, date, time, page, limit);
        PageResponse<UserServiceResponseDto> services = getServiceDataQueryHandler.handle(query);
        return ResponseEntity
                .ok(new ApiResponse<>(true, ServiceEntityConstant.GET_SERVICES_SUCCESS, services));
    }

    @GetMapping("/{serviceId}/staff")
    public ResponseEntity<ApiResponse<List<StaffServiceResponseDto>>> getAvailableStaffForService(
            @PathVariable String serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        GetAvailableStaffForServiceQuery query = new GetAvailableStaffForServiceQuery(serviceId, date, time);
        List<StaffServiceResponseDto> staff = getAvailableStaffForServiceQueryHandler.handle(query);
        return ResponseEntity
                .ok(new ApiResponse<>(true, ServiceEntityConstant.GET_AVAILABLE_STAFF_SUCCESS, staff));
    }

    @GetMapping("/{serviceId}/available-time-slots")
    public ResponseEntity<ApiResponse<List<AvailableTimeSlotResponseDto>>> getAvailableTimeSlots(
            @PathVariable String serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        GetAvailableTimeSlotsQuery query = new GetAvailableTimeSlotsQuery(serviceId, date);
        List<AvailableTimeSlotResponseDto> slots = getAvailableTimeSlotsQueryHandler.handle(query);
        return ResponseEntity
                .ok(new ApiResponse<>(true, ServiceEntityConstant.GET_AVAILABLE_TIME_SLOTS_SUCCESS, slots));
    }

}
