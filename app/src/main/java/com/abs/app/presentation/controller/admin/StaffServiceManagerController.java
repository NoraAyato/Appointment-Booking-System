package com.abs.app.presentation.controller.admin;

import com.abs.app.application.admin.staffservice.command.CreateStaffServiceCommand;
import com.abs.app.application.admin.staffservice.command.CreateStaffServiceCommandHandler;
import com.abs.app.application.admin.staffservice.command.DeleteStaffServiceCommandHandler;
import com.abs.app.application.admin.staffservice.dto.CreateStaffServiceRequestDto;
import com.abs.app.application.admin.staffservice.dto.StaffServiceResponseDto;
import com.abs.app.application.admin.staffservice.query.GetStaffServiceListQuery;
import com.abs.app.application.admin.staffservice.query.GetStaffServiceListQueryHandler;
import com.abs.app.common.constant.StaffServiceConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/staff-services")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StaffServiceManagerController {
    private final CreateStaffServiceCommandHandler createStaffServiceCommandHandler;
    private final DeleteStaffServiceCommandHandler deleteStaffServiceCommandHandler;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody CreateStaffServiceRequestDto request) {
        createStaffServiceCommandHandler.handle(new CreateStaffServiceCommand(request.getStaffId(), request.getServiceId()));
        return ResponseEntity.ok(new ApiResponse<>(true, StaffServiceConstant.CREATE_SUCCESS, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deleteStaffServiceCommandHandler.handle(id);
        return ResponseEntity.ok(new ApiResponse<>(true, StaffServiceConstant.DELETE_SUCCESS, null));
    }
}
