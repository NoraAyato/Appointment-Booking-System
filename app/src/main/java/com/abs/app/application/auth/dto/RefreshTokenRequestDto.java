package com.abs.app.application.auth.dto;

import com.abs.app.common.constant.Messages;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDto {
    @NotBlank(message = Messages.INVALID_TOKEN)
    private String refreshToken;
}
