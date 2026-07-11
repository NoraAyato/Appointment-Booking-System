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

    public void validateBlockedSlotRule(LocalDate blockedDate, LocalTime startTime, LocalTime endTime) {
        boolean onlyOneTimeEmpty = startTime == null ^ endTime == null;
        if (onlyOneTimeEmpty) {
            throw new BusinessException(BlockedSlotConstant.TIME_RANGE_MUST_BE_BOTH_EMPTY_OR_BOTH_PROVIDED);
        }

        if (isAllDay(startTime, endTime)) {
            if (blockedDate == null) {
                throw new BusinessException(BlockedSlotConstant.BLOCKED_DATE_REQUIRED_FOR_ALL_DAY);
            }
            if (!dateTimeService.isValidDate(blockedDate)) {
                throw new BusinessException(BlockedSlotConstant.INVALID_DATE);
            }
            return;
        }

        if (!dateTimeService.isValidTimeRange(startTime, endTime)) {
            throw new BusinessException(BlockedSlotConstant.INVALID_TIME_RANGE);
        }

        if (blockedDate != null && !dateTimeService.isValidDate(blockedDate)) {
            throw new BusinessException(BlockedSlotConstant.INVALID_DATE);
        }
    }

    public boolean isAllDay(LocalTime startTime, LocalTime endTime) {
        return startTime == null && endTime == null;
    }

    public boolean isValidDateTimeRange(LocalDate blockedDate, LocalTime startTime, LocalTime endTime,
            List<BlockedSlot> existingSlots) {
        if (!isAllDay(startTime, endTime) && (startTime.isAfter(endTime) || startTime.equals(endTime))) {
            return false;
        }

        for (BlockedSlot slot : existingSlots) {
            boolean dateScopesOverlap = blockedDate == null
                    || slot.getBlockedDate() == null
                    || slot.getBlockedDate().equals(blockedDate);
            boolean timeScopesOverlap = isAllDay(startTime, endTime)
                    || isAllDay(slot.getStartTime(), slot.getEndTime())
                    || dateTimeService.isOverlapping(startTime, endTime, slot.getStartTime(), slot.getEndTime());

            if (dateScopesOverlap && timeScopesOverlap) {
                return false;
            }
        }

        return true;
    }
}
