package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.staffservice.dto.StaffServiceResponseDto;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.User;

import java.util.List;
import java.util.Map;

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

    public static com.abs.app.application.user.service.dto.StaffServiceResponseDto toUserStaffServiceResponse(
            StaffService staffService,
            Map<String, Double> ratingsByStaffId,
            Map<String, Integer> completedServicesByStaffId,
            Map<String, List<String>> specialtiesByStaffId) {
        if (staffService == null || staffService.getStaff() == null) {
            return null;
        }

        User staff = staffService.getStaff();
        String staffId = staff.getUserId();
        String firstName = staff.getFirstName() != null ? staff.getFirstName() : "";
        String lastName = staff.getLastName() != null ? staff.getLastName() : "";

        com.abs.app.application.user.service.dto.StaffServiceResponseDto dto =
                new com.abs.app.application.user.service.dto.StaffServiceResponseDto();
        dto.setId(staffId);
        dto.setStaffName((firstName + " " + lastName).trim());
        dto.setStaffAvatar(staff.getPicture());
        dto.setRating(ratingsByStaffId.getOrDefault(staffId, 0D));
        dto.setCompletedServices(completedServicesByStaffId.getOrDefault(staffId, 0));
        dto.setSpecialties(specialtiesByStaffId.getOrDefault(staffId, List.of()));

        return dto;
    }
}
