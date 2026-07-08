package com.abs.app.presentation.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.service.dto.UserServiceResponseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/public/services")
@RequiredArgsConstructor
public class ServiceController {
    private final GetServiceDataQueryHandler getServiceDataQueryHandler;
    private final GetTopRatedServicesQueryHandler getTopRatedServicesQueryHandler;

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

}
