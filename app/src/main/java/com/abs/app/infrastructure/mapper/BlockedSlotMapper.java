package com.abs.app.infrastructure.mapper;

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
}
