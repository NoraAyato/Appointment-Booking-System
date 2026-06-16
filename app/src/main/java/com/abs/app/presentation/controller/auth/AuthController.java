package com.abs.app.presentation.controller.auth;

import com.abs.app.application.auth.command.RegisterUserCommand;
import com.abs.app.application.auth.command.RegisterUserCommandHandler;
import com.abs.app.application.auth.command.ResetPasswordCommand;
import com.abs.app.application.auth.command.ResetPasswordCommandHandler;
import com.abs.app.application.auth.command.VerifyOtpCommand;
import com.abs.app.application.auth.command.VerifyOtpCommandHandler;
import com.abs.app.application.auth.dto.RegisterRequestDto;
import com.abs.app.application.auth.dto.ResetPasswordRequestDto;
import com.abs.app.application.auth.dto.VerifyOtpRequestDto;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.auth.command.ChangePasswordCommand;
import com.abs.app.application.auth.command.ChangePasswordCommandHandler;
import com.abs.app.application.auth.command.ForgotPasswordCommand;
import com.abs.app.application.auth.command.ForgotPasswordCommandHandler;
import com.abs.app.application.auth.command.GenerateOtpCommand;
import com.abs.app.application.auth.command.GenerateOtpCommandHandler;
import com.abs.app.application.auth.command.GoogleCallbackCommand;
import com.abs.app.application.auth.command.GoogleCallbackCommandHandler;
import com.abs.app.application.auth.command.GoogleLoginCommand;
import com.abs.app.application.auth.command.GoogleLoginCommandHandler;
import com.abs.app.application.auth.command.LoginUserCommand;
import com.abs.app.application.auth.command.LoginUserCommandHandler;
import com.abs.app.application.auth.command.RefreshTokenCommand;
import com.abs.app.application.auth.command.RefreshTokenCommandHandler;
import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.application.auth.dto.ChangePasswordRequestDto;
import com.abs.app.application.auth.dto.ForgotPasswordRequestDto;
import com.abs.app.application.auth.dto.GenerateOtpRequestDto;
import com.abs.app.application.auth.dto.GenerateOtpResponseDto;
import com.abs.app.application.auth.dto.GoogleLoginRequestDto;
import com.abs.app.application.auth.dto.LoginRequestDto;
import com.abs.app.application.auth.dto.RefreshTokenRequestDto;
import com.abs.app.common.constant.Messages;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.infrastructure.security.SecurityUtils;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginUserCommandHandler loginHandler;
    private final RegisterUserCommandHandler registerHandler;
    private final ChangePasswordCommandHandler changePasswordHandler;
    private final ForgotPasswordCommandHandler forgotPasswordCommandHandler;
    private final RefreshTokenCommandHandler refreshTokenCommandHandler;
    private final GoogleLoginCommandHandler googleLoginCommandHandler;
    private final ResetPasswordCommandHandler resetPasswordCommandHandler;
    private final GenerateOtpCommandHandler generateOtpCommandHandler;
    private final VerifyOtpCommandHandler verifyOtpCommandHandler;
    private final GoogleCallbackCommandHandler googleCallbackCommandHandler;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto dto) {
        AuthResponseDto response = loginHandler
                .handle(new LoginUserCommand(dto.getEmail(), dto.getPassword(), dto.isRememberMe()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.LOGIN_SUCCESS, response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@Valid @RequestBody RegisterRequestDto dto) {
        AuthResponseDto responseDto = registerHandler.handle(
                new RegisterUserCommand(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.REGISTER_SUCCESS, responseDto));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<AuthResponseDto>> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto dto) {
        String userId = SecurityUtils.getCurrentUserId();
        AuthResponseDto response = changePasswordHandler.handle(new ChangePasswordCommand(
                userId, dto.getCurrentPassword(), dto.getNewPassword(), dto.getRePassword()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.CHANGE_PASSWORD_SUCCESS, response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto dto) {
        forgotPasswordCommandHandler.handle(new ForgotPasswordCommand(dto.getEmail()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.FORGOT_PASSWORD_SUCCESS, null));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponseDto>> refreshToken(@RequestBody RefreshTokenRequestDto dto) {
        AuthResponseDto response = refreshTokenCommandHandler.handle(new RefreshTokenCommand(dto.getRefreshToken()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.REFRESH_TOKEN_SUCCESS, response));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthResponseDto>> googleLogin(
            @Valid @RequestBody GoogleLoginRequestDto dto) {
        AuthResponseDto response = googleLoginCommandHandler.handle(
                new GoogleLoginCommand(dto.getIdToken()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.GOOGLE_LOGIN_SUCCESS, response));
    }

    @GetMapping("/google/callback")
    public void googleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        var result = googleCallbackCommandHandler.handle(new GoogleCallbackCommand(code));

        if (result.isSuccess()) {
            String redirectUrl = "http://localhost:5173/auth/google-callback" +
                    "?accessToken="
                    + result.getAuthResponse().getAccessToken()
                    + "&refreshToken=" + result.getAuthResponse().getRefreshToken();
            response.sendRedirect(redirectUrl);
        } else {
            String redirectUrl = "http://localhost:5173/auth/google-callback?error="
                    + java.net.URLEncoder.encode(result.getErrorMessage(), "UTF-8");
            response.sendRedirect(redirectUrl);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto dto) {
        resetPasswordCommandHandler.handle(new ResetPasswordCommand(dto.getToken(), dto.getNewPassword()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.CHANGE_PASSWORD_SUCCESS, null));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<GenerateOtpResponseDto>> sendOtp(
            @Valid @RequestBody GenerateOtpRequestDto dto) {
        GenerateOtpResponseDto response = generateOtpCommandHandler.handle(new GenerateOtpCommand(dto.getEmail()));
        return ResponseEntity.ok(new ApiResponse<>(true, response.getMessage(), response));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto dto) {
        verifyOtpCommandHandler.handle(new VerifyOtpCommand(dto.getEmail(), dto.getOtp()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.VERIFY_OTP_SUCCESS, null));
    }
}
