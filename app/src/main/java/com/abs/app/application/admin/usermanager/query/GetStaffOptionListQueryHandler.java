package com.abs.app.application.admin.usermanager.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abs.app.application.admin.usermanager.dto.UserOptionResponse;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetStaffOptionListQueryHandler {
    private final UserRepository userRepository;

    public List<UserOptionResponse> handle() {
        return userRepository.findAll().stream().filter(user -> user.getRole().getRoleName().name().equals("STAFF"))
                .map(UserMapper::toUserOptionResponse)
                .toList();
    }
}
