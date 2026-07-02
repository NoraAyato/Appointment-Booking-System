package com.abs.app.application.auth.command;

import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.UserLogin;
import com.abs.app.domain.entity.enums.LoginProvider;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.UserLoginRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.RefreshTokenService;
import com.abs.app.infrastructure.security.JwtTokenProvider;
// import com.abs.app.infrastructure.service.ActivityLogHelper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.common.constant.Messages;
import com.abs.app.common.exception.UnauthorizedException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleLoginCommandHandler {

    private final GoogleIdTokenVerifier googleVerifier;
    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    // private final ActivityLogHelper activityLogHelper;

    public AuthResponseDto handle(GoogleLoginCommand command) {
        String idTokenStr = command.getIdToken();

        GoogleIdToken idToken;
        try {
            idToken = googleVerifier.verify(idTokenStr);
        } catch (Exception e) {
            throw new UnauthorizedException("Xác thực token với Google thất bại.");
        }

        if (idToken == null) {
            throw new UnauthorizedException("ID Token không hợp lệ.");
        }

        Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        String providerId = payload.getSubject();

        Optional<UserLogin> optionalUserLogin = userLoginRepository.findByProviderAndProviderId(LoginProvider.GOOGLE,
                providerId);
        User user;
        if (optionalUserLogin.isPresent()) {
            user = optionalUserLogin.get().getUser();
        } else {
            user = userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User(email, name);
                // activityLogHelper.logUserRegistered(newUser.getUserId(), email);
                return userRepository.save(newUser);
            });

            UserLogin userLogin = new UserLogin(null, LoginProvider.GOOGLE, providerId, user);
            userLoginRepository.save(userLogin);
        }
        if (!user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new UnauthorizedException(Messages.PROHIBIT_ACCOUNT_MESSAGE);
        }
        String accessToken = jwtTokenProvider.generateToken(user.getUserId(), user.getRole().getRoleName().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());
        refreshTokenService.save(user.getUserId(), refreshToken, 60 * 24 * 3);
        // activityLogHelper.logUserLogin(user.getUserName(), user.getUserId());
        return new AuthResponseDto(accessToken, refreshToken);
    }
}
