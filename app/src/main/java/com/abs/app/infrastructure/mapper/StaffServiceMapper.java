package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.staffservice.dto.StaffServiceResponseDto;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.User;

public class StaffServiceMapper {
    public static StaffServiceResponseDto toStaffServiceResponse(StaffService staffService) {
        if (staffService == null)
            return null;

        StaffServiceResponseDto dto = new StaffServiceResponseDto();
        dto.setId(staffService.getId());
        dto.setStatus(staffService.getStatus().name());

        User staff = staffService.getStaff();
        String firstName = staff.getFirstName() != null ? staff.getFirstName() : "";
        String lastName = staff.getLastName() != null ? staff.getLastName() : "";
        dto.setStaffName((firstName + " " + lastName).trim());
        dto.setStaffAvatar(staff.getPicture());
        ServiceEntity service = staffService.getService();
        dto.setServiceName(service.getName());

        return dto;
    }
}
