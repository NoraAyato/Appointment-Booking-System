package com.abs.app.application.auth.command;

import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.common.constant.Messages;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.exception.UnauthorizedException;
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
        User user = userRepo.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

        if (!passwordEncoder.matches(command.getCurrentPassword(), user.getPassWord())) {
            throw new UnauthorizedException(Messages.INVALID_CURRENT_PASSWORD);
        }

        if (!command.getNewPassword().equals(command.getRePassword())) {
            throw new UnauthorizedException(Messages.INVALID_RE_PASSWORD);
        }
        user.setPassWord(passwordEncoder.encode(command.getNewPassword()));
        userRepo.save(user);
        String accessToken = jwtTokenProvider.generateToken(user.getUserId(), user.getRole().getRoleName().toString());
        return new AuthResponseDto(accessToken, null);
    }
}