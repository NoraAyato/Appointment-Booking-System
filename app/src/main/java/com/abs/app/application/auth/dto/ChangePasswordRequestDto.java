package com.abs.app.application.auth.dto;

import com.abs.app.common.constant.Messages;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequestDto {
    @NotBlank(message = Messages.EMAIL_REQUIRED)
    private String email;

    @NotBlank(message = Messages.CURRENT_PASSWORD_REQUIRED)
    private String currentPassword;

    @NotBlank(message = Messages.NEW_PASSWORD_REQUIRED)
    private String newPassword;

    @NotBlank(message = Messages.CONFIRM_PASSWORD_REQUIRED)
    private String rePassword;

}