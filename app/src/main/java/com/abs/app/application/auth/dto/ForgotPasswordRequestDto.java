package com.abs.app.application.auth.dto;

import com.abs.app.common.constant.Messages;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequestDto {
    @NotBlank(message = Messages.EMAIL_REQUIRED)
    @Email(message = Messages.EMAIL_INVALID)
    private String email;
}