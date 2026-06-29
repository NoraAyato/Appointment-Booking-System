package com.abs.app.domain.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockedSlotService {
    private final DateTimeService dateTimeService;
    // public BlockedSlotStatus handleStatus(String status) {
    // return switch (status) {

    // default -> throw new IllegalArgumentException("Invalid status: " + status);
    // };
    // } // SOLID
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
