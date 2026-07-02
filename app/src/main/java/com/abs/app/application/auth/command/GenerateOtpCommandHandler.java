package com.abs.app.application.auth.command;

import com.abs.app.application.auth.dto.GenerateOtpResponseDto;
import com.abs.app.common.constant.Messages;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.EmailService;
import com.abs.app.domain.service.OtpTokenService;

import lombok.RequiredArgsConstructor;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateOtpCommandHandler {

    private final EmailService emailService;
    private final OtpTokenService otpTokenService;
    private final UserRepository userService;

    public GenerateOtpResponseDto handle(GenerateOtpCommand command) {
        var userOpt = userService.findByEmail(command.getEmail());
        if (userOpt.isPresent()) {
            throw new DuplicateResourceException(UserConstant.USER_NOT_EXIST);
        }
        String otp = generateOTP();
        String message = Messages.SEND_OTP_SUCCESS;
        emailService.sendVerifyOtp(command.getEmail(), otp);
        otpTokenService.saveOtp(command.getEmail(), otp, 1);
        return new GenerateOtpResponseDto(message, otp);
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
