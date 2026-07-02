package com.abs.app.presentation.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abs.app.application.user.command.UpdateUserImageCommand;
import com.abs.app.application.user.command.UpdateUserImageCommandHandler;
import com.abs.app.application.user.command.UpdateUserProfileCommand;
import com.abs.app.application.user.command.UpdateUserProfileCommandHandler;
import com.abs.app.application.user.dto.UpdateProfileRequestDto;
import com.abs.app.application.user.dto.UserInfoResponeDto;
import com.abs.app.application.user.query.GetCurrentUserQueryHandler;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.infrastructure.security.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final GetCurrentUserQueryHandler getCurrentUserQueryHandler;
    private final UpdateUserProfileCommandHandler updateUserProfileCommandHandler;
    private final UpdateUserImageCommandHandler updateUserImageCommandHandler;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponeDto>> getCurrentUser() {
        String userId = SecurityUtils.getCurrentUserId();
        UserInfoResponeDto userInfo = getCurrentUserQueryHandler.handle(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, UserConstant.GET_USER_INFO_SUCCESS, userInfo));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto dto) {
        String userId = SecurityUtils.getCurrentUserId();
        updateUserProfileCommandHandler.handle(new UpdateUserProfileCommand(
                userId,
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPhoneNumber(),
                dto.isGender()));
        return ResponseEntity.ok(new ApiResponse<>(true, UserConstant.UPDATE_USER_INFO_SUCCESS, null));
    }

    @PutMapping("/update-image")
    public ResponseEntity<ApiResponse<Void>> updatePicture(@RequestParam("file") MultipartFile file) {
        String userId = SecurityUtils.getCurrentUserId();
        updateUserImageCommandHandler.handle(new UpdateUserImageCommand(userId, file));
        return ResponseEntity.ok(new ApiResponse<>(true, UserConstant.UPDATE_USER_IMAGE_SUCCESS, null));
    }
}
