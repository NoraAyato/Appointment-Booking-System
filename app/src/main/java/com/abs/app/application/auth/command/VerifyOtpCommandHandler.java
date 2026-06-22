package com.abs.app.application.auth.command;

import org.springframework.stereotype.Component;

import com.abs.app.common.constant.Messages;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.service.OtpTokenService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VerifyOtpCommandHandler {
    private final OtpTokenService otpTokenService;

    public void handle(VerifyOtpCommand command) {
        boolean isValid = otpTokenService.verifyOtp(command.getEmail(), command.getOtp());
        if (!isValid) {
            throw new BusinessException(Messages.INVALID_OTP);
        }
        otpTokenService.invalidateOtp(command.getEmail());

    }
}
