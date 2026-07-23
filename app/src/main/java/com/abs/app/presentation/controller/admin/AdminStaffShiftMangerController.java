package com.abs.app.presentation.controller.admin;

import com.abs.app.application.admin.staffshift.command.*;
import com.abs.app.application.admin.staffshift.dto.AdminStaffShiftResponseDto;
import com.abs.app.application.admin.staffshift.dto.CreateBulkStaffShiftRequestDto;
import com.abs.app.application.admin.staffshift.dto.CreateStaffShiftRequestDto;
import com.abs.app.application.admin.staffshift.dto.UpdateStaffShiftRequestDto;
import com.abs.app.application.admin.staffshift.query.GetStaffShiftListQuery;
import com.abs.app.application.admin.staffshift.query.GetStaffShiftListQueryHandler;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/staff-shifts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStaffShiftMangerController {
    private final CreateStaffShiftCommandHandler createStaffShiftCommandHandler;
    private final UpdateStaffShiftCommandHandler updateStaffShiftCommandHandler;
    private final GetStaffShiftListQueryHandler getStaffShiftListQueryHandler;
    private final CreateBulkStaffShiftCommandHandler createBulkStaffShiftCommandHandler;

    @GetMapping()
    public ResponseEntity<ApiResponse<PageResponse<AdminStaffShiftResponseDto>>> getStaffShift(
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PageResponse<AdminStaffShiftResponseDto> staffShifts = getStaffShiftListQueryHandler.handle(new GetStaffShiftListQuery(
                keyWord,
                status,
                page,
                limit
        ));

        return ResponseEntity.ok(new ApiResponse<>(true, StaffShiftConstant.GET_LIST_SUCCESS, staffShifts));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody CreateStaffShiftRequestDto request) {
        createStaffShiftCommandHandler.handle(new CreateStaffShiftCommand(
                request.getStaffId(),
                request.getWorkDate(),
                request.getStartTime(),
                request.getEndTime()
        ));

        return ResponseEntity.ok(new ApiResponse<>(true, StaffShiftConstant.CREATE_SUCCESS, null));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<Void>> createWorkWeek(@Valid @RequestBody CreateBulkStaffShiftRequestDto request) {
        createBulkStaffShiftCommandHandler.handle(new CreateBulkStaffShiftCommand(
                request.getStartDate(),
                request.getEndDate(),
                request.getWorkingDays(),
                request.getStartTime(),
                request.getEndTime()
        ));

        return ResponseEntity.ok(new ApiResponse<>(true, StaffShiftConstant.CREATE_SUCCESS, null));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @Valid @RequestBody UpdateStaffShiftRequestDto request) {
        updateStaffShiftCommandHandler.handle(new UpdateStaffShiftCommand(
                id,
                request.getStaffId(),
                request.getWorkDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getStatus()
        ));

        return ResponseEntity.ok(new ApiResponse<>(true, StaffShiftConstant.UPDATE_SUCCESS, null));
    }
}
