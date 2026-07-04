package com.abs.app.application.auth.dto;

import com.abs.app.common.constant.AuthConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class VerifyOtpRequestDto {
    @NotBlank(message = AuthConstant.EMAIL_REQUIRED)
    private String email;

    @NotBlank(message = AuthConstant.OTP_REQUIRED)
    private String otp;
}
