package com.abs.app.application.auth.command;

import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordCommandHandler {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // handle change password when isLogin
    public AuthResponseDto handle(ChangePasswordCommand command) {
        Optional<User> userOptional = userRepo.findById(command.getUserId()); // Tìm bằng userId
        User user = userOptional
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID này !"));

        if (!passwordEncoder.matches(command.getCurrentPassword(), user.getPassWord())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng !");
        }

        if (!command.getNewPassword().equals(command.getRePassword())) {
            throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không khớp !");
        }
        user.setPassWord(passwordEncoder.encode(command.getNewPassword()));
        userRepo.save(user);
        String accessToken = jwtTokenProvider.generateToken(user.getUserId(), user.getRole().toString());
        return new AuthResponseDto(accessToken, null);
    }
}