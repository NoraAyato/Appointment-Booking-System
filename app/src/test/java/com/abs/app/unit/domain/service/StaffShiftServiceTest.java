package com.abs.app.unit.domain.service;

import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.StaffShiftRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StaffShiftServiceTest {

        private static final String STAFF_ID = "u_staff_001";
        private static final LocalDate WORK_DATE = LocalDate.now().plusDays(1);

        @Mock
        private BlockedSlotRepository blockedSlotRepository;
        @Mock
        private StaffShiftRepository staffShiftRepository;

        private StaffShiftService staffShiftService;
        private User staff;

        @BeforeEach
        void setup() {
                staffShiftService = new StaffShiftService(new DateTimeService(), blockedSlotRepository,
                                staffShiftRepository);
                staff = new User();
                staff.setUserId(STAFF_ID);
        }

        @Test
        void shouldAllowShiftThatContainsTimedBlockedSlot() {
                when(blockedSlotRepository.existsAllDayOnDateByStatus(
                                STAFF_ID,
                                WORK_DATE,
                                BlockedSlotStatus.APPROVED))
                                .thenReturn(false);

                assertDoesNotThrow(() -> staffShiftService.customValidateStaffShiftTime(
                                staff,
                                WORK_DATE,
                                LocalTime.of(9, 0),
                                LocalTime.of(17, 0),
                                null));

                verify(blockedSlotRepository).existsAllDayOnDateByStatus(
                                STAFF_ID,
                                WORK_DATE,
                                BlockedSlotStatus.APPROVED);
                verify(blockedSlotRepository, never()).existsOverlappingByStatus(
                                org.mockito.ArgumentMatchers.anyString(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any());
        }

        @Test
        void shouldRejectShiftWhenAllDayBlockedSlotExists() {
                when(blockedSlotRepository.existsAllDayOnDateByStatus(
                                STAFF_ID,
                                WORK_DATE,
                                BlockedSlotStatus.APPROVED))
                                .thenReturn(true);

                assertThrows(BusinessException.class, () -> staffShiftService.customValidateStaffShiftTime(
                                staff,
                                WORK_DATE,
                                LocalTime.of(9, 0),
                                LocalTime.of(17, 0),
                                null));
        }
}
