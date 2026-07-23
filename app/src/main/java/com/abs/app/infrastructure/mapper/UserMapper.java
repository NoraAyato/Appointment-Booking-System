package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.usermanager.dto.UserOptionResponse;
import com.abs.app.application.admin.usermanager.dto.UserResponseDto;
import com.abs.app.application.user.info.dto.UserInfoResponseDto;
import com.abs.app.domain.entity.User;

public class UserMapper {
    public static UserInfoResponseDto toUserInfoResponseDto(User user) {
        return new UserInfoResponseDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPicture(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus().name(),
                user.getRole().getRoleName().toString(),
                user.isGender(),
                user.isRecieveEmail());
    }

    public static UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().getRoleName().toString(),
                user.getStatus().name(),
                user.getCreatedAt().toString(),
                user.getPicture());
    }

    public static UserOptionResponse toUserOptionResponse(User user) {
        return new UserOptionResponse(user.getUserId(), user.getFirstName() + " " + user.getLastName());
    }
}
