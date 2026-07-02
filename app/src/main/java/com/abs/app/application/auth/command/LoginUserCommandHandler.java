package com.abs.app.application.auth.command;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abs.app.common.constant.Messages;
import com.abs.app.common.exception.UnauthorizedException;
import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.RefreshTokenService;
import com.abs.app.infrastructure.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUserCommandHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthResponseDto handle(LoginUserCommand command) {
        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new UnauthorizedException(Messages.INVALID_USERNAME_OR_PASSWORD));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassWord())) {
            throw new UnauthorizedException(Messages.INVALID_USERNAME_OR_PASSWORD);
        }
        String accessToken = jwtTokenProvider.generateToken(user.getUserId(), user.getRole().getRoleName().toString());
        if (command.isRememberMe()) {
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());
            refreshTokenService.save(user.getUserId(), refreshToken, 60 * 24 * 3);

            return new AuthResponseDto(accessToken, refreshToken);
        }
        return new AuthResponseDto(accessToken, null);
    }
}