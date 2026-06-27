package com.abs.app.domain.service;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.RoleConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.enums.RoleEnum;

@Service
public class RoleService {
    public RoleEnum handleRole(String role) {
        return switch (role) {
            case "ADMIN" -> RoleEnum.ADMIN;
            case "USER" -> RoleEnum.CUSTOMER;
            case "STAFF" -> RoleEnum.STAFF;
            default -> throw new BusinessException(RoleConstant.INVALID_USER_ROLE);
        };
    }
}
