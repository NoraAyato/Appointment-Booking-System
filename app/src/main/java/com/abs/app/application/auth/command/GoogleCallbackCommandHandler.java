package com.abs.app.application.auth.command;

import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.application.auth.dto.AuthCallbackResult;
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
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class GoogleCallbackCommandHandler {

    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final GoogleIdTokenVerifier googleVerifier;
    // private final ActivityLogHelper activityLogHelper;
    @Value("${google.oauth.client-id}")
    private String clientId;

    @Value("${google.oauth.client-secret}")
    private String clientSecret;

    @Value("${google.oauth.redirect-uri}")
    private String redirectUri;

    public AuthCallbackResult handle(GoogleCallbackCommand command) {
        try {
            String code = command.getCode();
            // Exchange code for tokens
            RestTemplate restTemplate = new RestTemplate();
            String tokenUrl = "https://oauth2.googleapis.com/token";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("grant_type", "authorization_code");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            String idTokenStr = (String) response.getBody().get("id_token");

            GoogleIdToken idToken;
            try {
                idToken = googleVerifier.verify(idTokenStr);
            } catch (Exception e) {
                return AuthCallbackResult.failure("Xác thực token với Google thất bại.");
            }

            if (idToken == null) {
                return AuthCallbackResult.failure("ID Token không hợp lệ.");
            }

            Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");
            String providerId = payload.getSubject();

            Optional<UserLogin> optionalUserLogin = userLoginRepository.findByProviderAndProviderId(
                    LoginProvider.GOOGLE,
                    providerId);
            User user;

            if (optionalUserLogin.isPresent()) {
                user = optionalUserLogin.get().getUser();
            } else {
                user = userRepository.findByEmail(email).orElseGet(() -> {
                    User newUser = new User(email, name);
                    newUser.setPicture(picture);
                    // activityLogHelper.logUserRegistered(newUser.getUserId(), email);
                    return userRepository.save(newUser);
                });

                UserLogin userLogin = new UserLogin(null, LoginProvider.GOOGLE, providerId, user);
                userLoginRepository.save(userLogin);
            }
            if (!user.getStatus().equals(UserStatus.ACTIVE)) {
                return AuthCallbackResult.failure("Tài khoản của bạn đã bị vô hiệu hóa.");
            }
            String accessToken = jwtTokenProvider.generateToken(user.getUserId(), user.getRole().toString());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());
            refreshTokenService.save(user.getUserId(), refreshToken, 60 * 24 * 3);
            // activityLogHelper.logUserLogin(user.getUserName(), user.getUserId());
            return AuthCallbackResult.success(new AuthResponseDto(accessToken, refreshToken));
        } catch (Exception e) {
            return AuthCallbackResult.failure(e.getMessage() != null ? e.getMessage() : "Đăng nhập Google thất bại");
        }
    }
}