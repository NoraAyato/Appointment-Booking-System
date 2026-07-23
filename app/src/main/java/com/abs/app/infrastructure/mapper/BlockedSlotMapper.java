package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.blockedslot.dto.AdminBlockedSlotResponseDto;
import com.abs.app.application.staff.blockedslot.dto.BlockedSlotResponseDto;
import com.abs.app.domain.entity.BlockedSlot;

public class BlockedSlotMapper {
    public static BlockedSlotResponseDto toBlockedSlotResponseDto(BlockedSlot blockedSlot) {
        BlockedSlotResponseDto responseDto = new BlockedSlotResponseDto();
        responseDto.setId(blockedSlot.getId());
        responseDto.setReason(blockedSlot.getReason());
        responseDto.setStatus(blockedSlot.getStatus().name());
        responseDto.setBlockedDate(blockedSlot.getBlockedDate());
        responseDto.setStartTime(blockedSlot.getStartTime());
        responseDto.setEndTime(blockedSlot.getEndTime());
        return responseDto;
    }

    public static AdminBlockedSlotResponseDto toAdminBlockedSlotResponseDto(
            BlockedSlot blockedSlot) {
        AdminBlockedSlotResponseDto responseDto = new AdminBlockedSlotResponseDto();
        responseDto.setId(blockedSlot.getId());
        responseDto.setReason(blockedSlot.getReason());
        responseDto.setStatus(blockedSlot.getStatus().name());
        responseDto.setBlockedDate(blockedSlot.getBlockedDate());
        responseDto.setStartTime(blockedSlot.getStartTime());
        responseDto.setEndTime(blockedSlot.getEndTime());
        responseDto.setAppliesEveryDay(blockedSlot.getBlockedDate() == null);
        responseDto.setAllDay(blockedSlot.getStartTime() == null && blockedSlot.getEndTime() == null);
        var staff = blockedSlot.getStaff();
        responseDto.setAppliesToAllStaff(staff == null);
        if (staff != null) {
            String staffName = staff != null ? staff.getFirstName() + " " + staff.getLastName() : null;
            responseDto.setStaffName(staffName);
            responseDto
                    .setAvatarUrl(staff != null && staff.getPicture() != null ? staff.getPicture() : null);
        }
        return responseDto;
    }
}
