package com.abs.app.domain.service;

import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.StaffShift;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffShiftService {
    private final DateTimeService dateTimeService;

    public boolean isValidDateTimeRange(LocalDate workDate, LocalTime startTime, LocalTime endTime, List<StaffShift> existingWorkDate) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            return false;
        }

        for (StaffShift date : existingWorkDate) {
            if (date.getWorkDate().equals(workDate)
                    && dateTimeService.isOverlapping(startTime, endTime, date.getStartTime(), date.getEndTime())) {
                return false;
            }
        }

        return true;
    }

    public boolean isValidWorkDateOverlapWithBlockedSlot(LocalDate workDate, LocalTime startTime, LocalTime endTime, List<BlockedSlot> blockedSlots) {
        for (BlockedSlot slot : blockedSlots) {
            if (workDate.equals(slot.getBlockedDate()) && dateTimeService.isOverlapping(startTime, endTime, slot.getStartTime(), slot.getEndTime())) {
                return false;
            }
        }

        return true;
    }
}
