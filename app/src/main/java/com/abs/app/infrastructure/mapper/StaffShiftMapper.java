package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.staffshift.dto.AdminStaffShiftResponseDto;
import com.abs.app.application.staff.staffshift.dto.StaffShiftResponseDto;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;

import java.util.Collections;
import java.util.List;

public class StaffShiftMapper {
    public static AdminStaffShiftResponseDto toAdminStaffShiftResponseDto(StaffShift staffShift) {
        AdminStaffShiftResponseDto dto = new AdminStaffShiftResponseDto();
        dto.setId(staffShift.getId());
        dto.setWorkDate(staffShift.getWorkDate());
        dto.setStartTime(staffShift.getStartTime());
        dto.setEndTime(staffShift.getEndTime());
        dto.setStatus(staffShift.getStatus().name());

        User staff = staffShift.getStaff();
        if (staff != null) {
            dto.setStaffName(staff.getFirstName() + " " + staff.getLastName());
            dto.setStaffAvatar(staff.getPicture());

            List<StaffService> staffServices = staff.getStaffServices();
            if (staffServices != null && !staffServices.isEmpty()) {
                List<String> serviceNames = staffServices.stream()
                        .map(item -> item.getService().getName())
                        .toList();
                dto.setServiceNames(serviceNames);
            } else {
                dto.setServiceNames(Collections.emptyList());
            }
        }

        return dto;
    }

    public static StaffShiftResponseDto toStaffShiftResponseDto(StaffShift staffShift) {
        StaffShiftResponseDto dto = new StaffShiftResponseDto();
        dto.setId(staffShift.getId());
        dto.setWorkDate(staffShift.getWorkDate());
        dto.setStartTime(staffShift.getStartTime());
        dto.setEndTime(staffShift.getEndTime());
        dto.setStatus(staffShift.getStatus().name());

        User staff = staffShift.getStaff();
        List<StaffService> staffServices = staff.getStaffServices();
        if (staffServices != null && !staffServices.isEmpty()) {
            List<String> serviceNames = staffServices.stream()
                    .map(item -> item.getService().getName())
                    .toList();
            dto.setServiceNames(serviceNames);
        } else {
            dto.setServiceNames(Collections.emptyList());
        }

        return dto;
    }
}
