package com.abs.app.application.auth.dto;

import com.abs.app.common.constant.AuthConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    @NotBlank(message = AuthConstant.INVALID_TOKEN)
    private String token;

    @NotBlank(message = AuthConstant.NEW_PASSWORD_REQUIRED)
    private String newPassword;
}