package com.abs.app.presentation.controller.staff;

import com.abs.app.application.staff.staffshift.command.CreateBulkStaffShiftCommand;
import com.abs.app.application.staff.staffshift.command.CreateBulkStaffShiftCommandHandler;
import com.abs.app.application.staff.staffshift.command.CreateStaffShiftCommand;
import com.abs.app.application.staff.staffshift.command.CreateStaffShiftCommandHandler;
import com.abs.app.application.staff.staffshift.dto.CreateBulkStaffShiftRequestDto;
import com.abs.app.application.staff.staffshift.dto.CreateStaffShiftRequestDto;
import com.abs.app.application.staff.staffshift.dto.StaffShiftResponseDto;
import com.abs.app.application.staff.staffshift.query.GetStaffShiftListQuery;
import com.abs.app.application.staff.staffshift.query.GetStaffShiftListQueryHandler;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import com.abs.app.infrastructure.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/staff/staff-shifts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffShiftManagerController {
    private final GetStaffShiftListQueryHandler getStaffShiftListQueryHandler;
    private final CreateStaffShiftCommandHandler createStaffShiftCommandHandler;
    private final CreateBulkStaffShiftCommandHandler createBulkStaffShiftCommandHandler;

    @GetMapping()
    public ResponseEntity<ApiResponse<PageResponse<StaffShiftResponseDto>>> getStaffShift(
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        String userId = SecurityUtils.getCurrentUserId();
        PageResponse<StaffShiftResponseDto> staffShifts = getStaffShiftListQueryHandler.handle(new GetStaffShiftListQuery(
                userId,
                keyWord,
                status,
                page,
                limit
        ));

        return ResponseEntity.ok(new ApiResponse<>(true, StaffShiftConstant.GET_LIST_SUCCESS, staffShifts));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody CreateStaffShiftRequestDto request) {
        String userId = SecurityUtils.getCurrentUserId();
        createStaffShiftCommandHandler.handle(new CreateStaffShiftCommand(
                userId,
                request.getWorkDate(),
                request.getStartTime(),
                request.getEndTime()
        ));

        return ResponseEntity.ok(new ApiResponse<>(true, StaffShiftConstant.CREATE_SUCCESS, null));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<Void>> createWorkWeek(@Valid @RequestBody CreateBulkStaffShiftRequestDto request) {
        String userId = SecurityUtils.getCurrentUserId();
        createBulkStaffShiftCommandHandler.handle(new CreateBulkStaffShiftCommand(
                userId,
                request.getStartDate(),
                request.getEndDate(),
                request.getWorkingDays(),
                request.getStartTime(),
                request.getEndTime()
        ));

        return ResponseEntity.ok(new ApiResponse<>(true, StaffShiftConstant.CREATE_SUCCESS, null));
    }
}
