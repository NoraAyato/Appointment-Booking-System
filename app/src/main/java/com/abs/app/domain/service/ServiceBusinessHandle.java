package com.abs.app.domain.service;

import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.enums.ServiceStatus;
import org.springframework.stereotype.Service;

@Service

public class ServiceBusinessHandle {
    public ServiceStatus handleStatus(String status) {
        return switch (status) {
            case "ACTIVE" -> ServiceStatus.ACTIVE;
            case "INACTIVE" -> ServiceStatus.INACTIVE;
            default -> throw new ResourceNotFoundException(ServiceEntityConstant.STATUS_NOT_EXIST);
        };
    }
}
