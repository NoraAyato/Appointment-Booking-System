package com.abs.app.application.auth.dto;

import com.abs.app.common.constant.Messages;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    @NotBlank(message = Messages.INVALID_TOKEN)
    private String token;

    @NotBlank(message = Messages.NEW_PASSWORD_REQUIRED)
    private String newPassword;
}