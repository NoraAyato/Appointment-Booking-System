package com.abs.app.application.user.query;

import org.springframework.stereotype.Service;

import com.abs.app.application.user.dto.UserInfoResponeDto;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCurrentUserQueryHandler {
    private final UserRepository userRepository;

    public UserInfoResponeDto handle(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        return new UserInfoResponeDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPicture(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus().name(),
                user.getRole().getRoleName().toString(),
                user.isGender(),
                user.isRecieveEmail());
    }
}
