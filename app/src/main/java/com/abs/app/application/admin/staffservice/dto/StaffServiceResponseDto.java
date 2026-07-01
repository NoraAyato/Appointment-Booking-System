package com.abs.app.application.admin.staffservice.dto;

import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.User;
import lombok.Data;

@Data
public class StaffServiceResponseDto {
    private Long id;
    private User staff;
    private ServiceEntity service;
}
