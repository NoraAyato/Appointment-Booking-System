package com.abs.app.application.admin.usermanager.command;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.RoleConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Role;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.RoleRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.RoleService;
import com.abs.app.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateUserInfoCommandHandler {
    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    public void handle(UpdateUserInfoCommand command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        if (command.getRole() != null) {
            RoleEnum roleName = roleService.handleRole(command.getRole());
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException(RoleConstant.ROLE_NOT_EXIST));
            user.setRole(role);
        }
        if (command.getStatus() != null) {
            UserStatus status = userService.handleStatus(command.getStatus());
            user.setStatus(status);
        }
        userRepository.save(user);
    }
}
