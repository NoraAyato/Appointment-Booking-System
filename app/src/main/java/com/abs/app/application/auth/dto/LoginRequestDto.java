package com.abs.app.application.auth.dto;

import com.abs.app.common.constant.Messages;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = Messages.EMAIL_REQUIRED)
    private String email;

    @NotBlank(message = Messages.PASSWORD_REQUIRED)
    private String password;

    private boolean rememberMe;

}
