package com.abs.app.application.auth.dto;

import com.abs.app.common.constant.AuthConstant;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequestDto {
    @NotBlank(message = AuthConstant.INVALID_TOKEN)
    private String idToken;
}
