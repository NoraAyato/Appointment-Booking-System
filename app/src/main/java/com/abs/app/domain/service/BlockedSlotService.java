package com.abs.app.domain.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.BlockedSlotConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockedSlotService {
    private final DateTimeService dateTimeService;

    public BlockedSlotStatus handleBlockedStatus(String status) {
        return switch (status) {
            case "PENDING" -> BlockedSlotStatus.PENDING;
            case "APPROVED" -> BlockedSlotStatus.APPROVED;
            case "REJECTED" -> BlockedSlotStatus.REJECTED;
            default -> throw new BusinessException(BlockedSlotConstant.INVALID_BLOCKED_SLOT_STATUS);
        };
    }

    public boolean isValidDateTimeRange(LocalDate blockedDate, LocalTime startTime, LocalTime endTime,
            List<BlockedSlot> existingSlots) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            return false;
        }

        for (BlockedSlot slot : existingSlots) {
            if (slot.getBlockedDate().equals(blockedDate)
                    && dateTimeService.isOverlapping(startTime, endTime, slot.getStartTime(), slot.getEndTime())) {
                return false;
            }
        }

        return true;
    }
}
