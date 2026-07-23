package com.abs.app.presentation.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.admin.usermanager.command.UpdateUserInfoCommand;
import com.abs.app.application.admin.usermanager.command.UpdateUserInfoCommandHandler;
import com.abs.app.application.admin.usermanager.dto.UpdateUserInfoRequest;
import com.abs.app.application.admin.usermanager.dto.UserOptionResponse;
import com.abs.app.application.admin.usermanager.dto.UserResponseDto;
import com.abs.app.application.admin.usermanager.dto.UserStatsResponseDto;
import com.abs.app.application.admin.usermanager.query.GetStaffOptionListQueryHandler;
import com.abs.app.application.admin.usermanager.query.GetUserListQuery;
import com.abs.app.application.admin.usermanager.query.GetUserListQueryHandler;
import com.abs.app.application.admin.usermanager.query.GetUserStatsQueryHandler;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserManagerController {
    private final GetUserListQueryHandler getUserListQueryHandler;
    private final GetUserStatsQueryHandler getUserStatsQueryHandler;
    private final UpdateUserInfoCommandHandler updateUserInfoCommandHandler;
    private final GetStaffOptionListQueryHandler getStaffOptionListQueryHandler;

    @GetMapping()
    public ResponseEntity<ApiResponse<PageResponse<UserResponseDto>>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        GetUserListQuery query = new GetUserListQuery(page, limit, role, status, search);
        PageResponse<UserResponseDto> userList = getUserListQueryHandler.handle(query);
        return ResponseEntity.ok(new ApiResponse<>(true, UserConstant.GET_USER_LIST_SUCCESS, userList));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUserInfo(@PathVariable String id,
            @RequestBody UpdateUserInfoRequest request) {
        updateUserInfoCommandHandler.handle(new UpdateUserInfoCommand(id, request.getRole(), request.getStatus()));
        return ResponseEntity.ok(new ApiResponse<>(true, UserConstant.UPDATE_USER_INFO_SUCCESS, null));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<UserStatsResponseDto>> getUserStats() {
        UserStatsResponseDto userStats = getUserStatsQueryHandler.handle();
        return ResponseEntity.ok(new ApiResponse<>(true, UserConstant.GET_USER_STATS_SUCCESS, userStats));
    }

    @GetMapping("/staff-options")
    public ResponseEntity<ApiResponse<List<UserOptionResponse>>> getStaffOptions() {
        List<UserOptionResponse> staffOptions = getStaffOptionListQueryHandler.handle();
        return ResponseEntity.ok(new ApiResponse<>(true, UserConstant.GET_STAFF_OPTIONS_SUCCESS, staffOptions));
    }
}
