package com.abs.app.application.auth.command;

import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.common.exception.UnauthorizedException;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.RefreshTokenService;
import com.abs.app.infrastructure.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommandHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public AuthResponseDto handle(RefreshTokenCommand command) {
        String refreshToken = command.getRefreshToken();
        String userId;
        try {
            userId = jwtTokenProvider.getUserId(refreshToken);
        } catch (Exception e) {
            throw new UnauthorizedException("Token không hợp lệ hoặc sai định dạng");
        }

        Optional<User> userRecent = userRepository.findById(userId);

        if (!refreshTokenService.isValid(userRecent.get().getUserId(), refreshToken)) {
            throw new UnauthorizedException("Refresh token không hợp lệ");
        }

        String newAccessToken = jwtTokenProvider.generateToken(
                userId,
                userRecent.orElseThrow(() -> new UnauthorizedException("User không tồn tại")).getRole().toString());

        // refreshTokenService.invalidate(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);
        refreshTokenService.save(userId, newRefreshToken, 60 * 24 * 3);
        System.out.println("RefreshToken: " + refreshToken);
        System.out.println("UserId from token: " + userId);
        System.out.println("Token tồn tại trong Redis: " + refreshTokenService.get(userId));
        System.out.println("Token so sánh: " + refreshToken.equals(refreshTokenService.get(userId)));

        return new AuthResponseDto(newAccessToken, newRefreshToken);
    }
}
