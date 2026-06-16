package com.abs.app.presentation.controller.auth;

import com.abs.app.application.auth.command.RegisterUserCommand;
import com.abs.app.application.auth.command.RegisterUserCommandHandler;
import com.abs.app.application.auth.dto.RegisterRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.auth.command.LoginUserCommand;
import com.abs.app.application.auth.command.LoginUserCommandHandler;
import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.application.auth.dto.LoginRequestDto;
import com.abs.app.common.constant.Messages;
import com.abs.app.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginUserCommandHandler loginHandler;
    private final RegisterUserCommandHandler registerHandler;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto dto) {
        AuthResponseDto response = loginHandler
                .handle(new LoginUserCommand(dto.getEmail(), dto.getPassword(), dto.isRememberMe()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.LOGIN_SUCCESS, response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@Valid @RequestBody RegisterRequestDto dto) {
        AuthResponseDto responseDto = registerHandler.handle(new RegisterUserCommand(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName()));
        return ResponseEntity.ok(new ApiResponse<>(true, Messages.REGISTER_SUCCESS, responseDto));
    }
}
