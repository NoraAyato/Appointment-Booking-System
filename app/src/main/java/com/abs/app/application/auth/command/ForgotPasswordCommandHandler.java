package com.abs.app.application.auth.command;

import com.abs.app.common.constant.Messages;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.abs.app.domain.service.OtpTokenService;
import com.abs.app.infrastructure.security.JwtTokenProvider;

import java.util.Optional;
import java.util.Random;

@Service
public class ForgotPasswordCommandHandler {
    private final EmailService emailService;
    private final UserRepository userService;
    private final OtpTokenService otpTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    public ForgotPasswordCommandHandler(EmailService emailService, UserRepository userService,
            OtpTokenService otpTokenService, JwtTokenProvider jwtTokenProvider) {
        this.emailService = emailService;
        this.userService = userService;
        this.otpTokenService = otpTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void handle(ForgotPasswordCommand command) {
        Optional<User> userOptional = userService.findByEmail(command.getEmail());
        User user = userOptional
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản phù hợp với email trên !"));
        String resetPasswordToken = jwtTokenProvider.generateResetPasswordToken(user.getUserId());
        emailService.sendResetPasswordEmail(user.getEmail(), resetPasswordToken);
        otpTokenService.saveOtp(user.getEmail(), resetPasswordToken, 1);
    }
}