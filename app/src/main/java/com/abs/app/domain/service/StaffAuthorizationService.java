package com.abs.app.domain.service;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;

@Service
public class StaffAuthorizationService {
    public void ensureStaff(User user) {
        if (user == null || user.getRole() == null || !RoleEnum.STAFF.equals(user.getRole().getRoleName())) {
            throw new BusinessException(StaffShiftConstant.ONLY_STAFF_ALLOWED);
        }
    }
}
