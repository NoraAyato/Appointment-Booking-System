package com.abs.app.application.auth.dto;

import com.abs.app.common.constant.Messages;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class VerifyOtpRequestDto {
    @NotBlank(message = Messages.EMAIL_REQUIRED)
    private String email;

    @NotBlank(message = Messages.OTP_REQUIRED)
    private String otp;
}
