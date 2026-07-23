package com.abs.app.unit.domain.service;

import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.service.DateTimeService;
import com.abs.app.domain.service.StaffShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StaffShiftServiceTest {

        private static final String STAFF_ID = "u_staff_001";
        private static final LocalDate WORK_DATE = LocalDate.now().plusDays(1);

    private StaffShiftService staffShiftService;
    private User staff;

    @BeforeEach
    void setup() {
        staffShiftService = new StaffShiftService(new DateTimeService());
        staff = new User();
        staff.setUserId(STAFF_ID);
    }

    @Test
    void shouldAllowShiftThatContainsTimedBlockedSlot() {
        assertDoesNotThrow(() -> staffShiftService.customValidateStaffShiftTime(
                staff,
                WORK_DATE,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                null,
                false));
    }

    @Test
    void shouldRejectShiftWhenAllDayBlockedSlotExists() {
        assertThrows(BusinessException.class, () -> staffShiftService.customValidateStaffShiftTime(
                staff,
                WORK_DATE,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                null,
                true));
    }
}
