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
        String staffName = blockedSlot.getStaff() != null
                ? blockedSlot.getStaff().getFirstName() + " " + blockedSlot.getStaff().getLastName()
                : null;
        responseDto.setStaffName(staffName);
        responseDto
                .setAvatarUrl(blockedSlot.getStaff().getPicture() != null ? blockedSlot.getStaff().getPicture() : null);
        return responseDto;
    }
}
