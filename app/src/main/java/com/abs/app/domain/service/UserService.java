package com.abs.app.domain.service;

import org.springframework.stereotype.Service;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.enums.UserStatus;

@Service
public class UserService {
    public UserStatus handleStatus(String status) {
        return switch (status) {
            case "ACTIVE" -> UserStatus.ACTIVE;
            case "INACTIVE" -> UserStatus.BLOCKED;
            default -> throw new BusinessException(UserConstant.INVALID_USER_STATUS);
        };
    }

}
