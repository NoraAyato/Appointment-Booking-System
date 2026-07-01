package com.abs.app.presentation.controller.staff;

import com.abs.app.application.staff.staffshift.command.CreateStaffShiftCommand;
import com.abs.app.application.staff.staffshift.command.CreateStaffShiftCommandHandler;
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
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/staff/staff-shifts")
@RequiredArgsConstructor
public class StaffShiftManagerController {
    private final GetStaffShiftListQueryHandler getStaffShiftListQueryHandler;
    private final CreateStaffShiftCommandHandler createStaffShiftCommandHandler;

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
}
