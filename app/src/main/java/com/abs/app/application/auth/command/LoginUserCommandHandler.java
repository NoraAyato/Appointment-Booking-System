package com.abs.app.application.auth.command;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.abs.app.common.exception.UnauthorizedException;
import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUserCommandHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDto handle(LoginUserCommand command) {
        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Sai tên đăng nhập hoặc mật khẩu !"));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassWord())) {
            throw new UnauthorizedException("Sai tên đăng nhập hoặc mật khẩu !");
        }
        String accessToken = jwtTokenProvider.generateToken(user.getUserId(), user.getRole().toString());
        if (command.isRememberMe()) {
            // String refreshToken =
            // jwtTokenProvider.generateRefreshToken(user.getUserId());
            // Lưu refresh token vào database hoặc cache nếu cần thiết
            // triển khai sau nếu có yêu cầu
            return new AuthResponseDto(accessToken, "");
        }
        return new AuthResponseDto(accessToken, null);
    }
}
