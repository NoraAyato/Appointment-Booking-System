package com.abs.app.application.auth.command;

import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.util.GenerateIdUtil;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.security.JwtTokenProvider;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserCommandHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDto handle(RegisterUserCommand command)
    {
        if(userRepository.existsByEmail(command.getEmail()))
        {
            throw new DuplicateResourceException("Email đã tồn tại");
        }
        User newUser = new User();
        newUser.setUserId(GenerateIdUtil.GenerateId());
        newUser.setUserName(command.getFirstName() + command.getLastName());
        newUser.setPassWord(passwordEncoder.encode(command.getPassword()));
        newUser.setFirstName(command.getFirstName());
        newUser.setLastName(command.getLastName());
        newUser.setEmail(command.getEmail());
        newUser.setUpdateAt(LocalDateTime.now());

        userRepository.save(newUser);

        String accessToken = jwtTokenProvider.generateToken(newUser.getUserId(), newUser.getRole().toString());

        return new AuthResponseDto(accessToken,null);
    }
}
