package com.abs.app.application.auth.command;

import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.common.constant.AuthConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.exception.UnauthorizedException;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.RefreshTokenService;
import com.abs.app.infrastructure.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
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
            throw new UnauthorizedException(AuthConstant.INVALID_TOKEN);
        }

        Optional<User> userRecent = userRepository.findById(userId);

        if (!refreshTokenService.isValid(userRecent.get().getUserId(), refreshToken)) {
            throw new UnauthorizedException(AuthConstant.INVALID_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.generateToken(
                userId,
                userRecent.orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST)).getRole()
                        .getRoleName()
                        .toString());

        // refreshTokenService.invalidate(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);
        refreshTokenService.save(userId, newRefreshToken, 60 * 24 * 3);
        return new AuthResponseDto(newAccessToken, newRefreshToken);
    }
}
