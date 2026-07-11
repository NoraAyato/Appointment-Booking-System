package com.abs.app.unit.application.user.service.query;

import com.abs.app.application.user.service.dto.AvailableTimeSlotResponseDto;
import com.abs.app.application.user.service.query.GetAvailableTimeSlotsQuery;
import com.abs.app.application.user.service.query.GetAvailableTimeSlotsQueryHandler;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.StaffShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAvailableTimeSlotsQueryHandlerTest {

    private static final String SERVICE_ID = "ser_001";
    private static final LocalDate DATE = LocalDate.of(2026, 7, 11);

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private StaffShiftRepository staffShiftRepository;

    @Mock
    private StaffServiceRepository staffServiceRepository;

    @InjectMocks
    private GetAvailableTimeSlotsQueryHandler handler;

    private ServiceEntity service;

    @BeforeEach
    void setup() {
        service = new ServiceEntity();
        service.setId(SERVICE_ID);
        service.setDurationMinutes(60);
        service.setStatus(ServiceStatus.ACTIVE);
    }

    @Test
    void shouldReturnOnlySlotsWithAvailableStaff() {
        StaffShift shift = new StaffShift();
        shift.setWorkDate(DATE);
        shift.setStartTime(LocalTime.of(9, 0));
        shift.setEndTime(LocalTime.of(11, 0));
        shift.setStatus(StaffShiftStatus.APPROVED);

        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service));
        when(staffShiftRepository.findApprovedShiftsForService(
                SERVICE_ID,
                DATE,
                ServiceStatus.ACTIVE,
                UserStatus.ACTIVE,
                StaffServiceStatus.ACTIVE,
                StaffShiftStatus.APPROVED))
                .thenReturn(List.of(shift));

        stubAvailableStaffCount(LocalTime.of(9, 0), LocalTime.of(10, 0), 2L);
        stubAvailableStaffCount(LocalTime.of(9, 30), LocalTime.of(10, 30), 0L);
        stubAvailableStaffCount(LocalTime.of(10, 0), LocalTime.of(11, 0), 1L);

        List<AvailableTimeSlotResponseDto> slots = handler.handle(new GetAvailableTimeSlotsQuery(SERVICE_ID, DATE));

        assertThat(slots).hasSize(2);
        assertThat(slots).extracting(AvailableTimeSlotResponseDto::getStartTime)
                .containsExactly(LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertThat(slots).extracting(AvailableTimeSlotResponseDto::getAvailableStaffCount)
                .containsExactly(2, 1);
    }

    @Test
    void shouldReturnEmptyListWhenServiceInactive() {
        service.setStatus(ServiceStatus.INACTIVE);
        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service));

        List<AvailableTimeSlotResponseDto> slots = handler.handle(new GetAvailableTimeSlotsQuery(SERVICE_ID, DATE));

        assertThat(slots).isEmpty();
        verify(staffShiftRepository, never()).findApprovedShiftsForService(
                eq(SERVICE_ID),
                eq(DATE),
                eq(ServiceStatus.ACTIVE),
                eq(UserStatus.ACTIVE),
                eq(StaffServiceStatus.ACTIVE),
                eq(StaffShiftStatus.APPROVED));
    }

    private void stubAvailableStaffCount(LocalTime startTime, LocalTime endTime, long count) {
        LocalDateTime startAt = DATE.atTime(startTime);
        LocalDateTime endAt = DATE.atTime(endTime);
        when(staffServiceRepository.countAvailableStaffForService(
                SERVICE_ID,
                DATE,
                startTime,
                endTime,
                startAt,
                endAt,
                ServiceStatus.ACTIVE,
                UserStatus.ACTIVE,
                StaffServiceStatus.ACTIVE,
                StaffShiftStatus.APPROVED,
                BlockedSlotStatus.APPROVED,
                AppointmentStatus.CANCELLED))
                .thenReturn(count);
    }
}
