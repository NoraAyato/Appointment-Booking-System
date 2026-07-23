package com.abs.app.application.admin.usermanager.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abs.app.application.admin.usermanager.dto.UserStatsResponseDto;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserStatsQueryHandler {
    private final UserRepository userRepository;

    public UserStatsResponseDto handle() {
        List<User> userList = userRepository.findAll();
        int totalUsers = userList.size();
        int activeUsers = (int) userList.stream()
                .filter(user -> user.getStatus().name().equals("ACTIVE"))
                .count();
        int inactiveUsers = totalUsers - activeUsers;
        return new UserStatsResponseDto(totalUsers, activeUsers, inactiveUsers);
    }
}
