package com.abs.app.unit.application.user.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abs.app.application.user.service.dto.StaffServiceResponseDto;
import com.abs.app.application.user.service.query.GetAvailableStaffForServiceQuery;
import com.abs.app.application.user.service.query.GetAvailableStaffForServiceQueryHandler;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.AppointmentDetailRepository;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.service.AppointmentHoldService;

@ExtendWith(MockitoExtension.class)
class GetAvailableStaffForServiceQueryHandlerTest {
    private static final String SERVICE_ID = "ser_001";
    private static final String STAFF_ID = "u_staff_001";
    private static final LocalDate DATE = LocalDate.now().plusDays(1);
    private static final LocalTime TIME = LocalTime.of(9, 0);

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private StaffServiceRepository staffServiceRepository;

    @Mock
    private ReviewsRepository reviewsRepository;

    @Mock
    private AppointmentDetailRepository appointmentDetailRepository;

    @Mock
    private AppointmentHoldService appointmentHoldService;

    @InjectMocks
    private GetAvailableStaffForServiceQueryHandler handler;

    private ServiceEntity service;
    private StaffService staffService;

    @BeforeEach
    void setup() {
        service = new ServiceEntity();
        service.setId(SERVICE_ID);
        service.setDurationMinutes(60);
        service.setStatus(ServiceStatus.ACTIVE);

        User staff = new User();
        staff.setUserId(STAFF_ID);
        staff.setFirstName("Nguyen");
        staff.setLastName("An");

        staffService = new StaffService();
        staffService.setStaff(staff);
        staffService.setService(service);
        staffService.setStatus(StaffServiceStatus.ACTIVE);
    }

    @Test
    void shouldReturnAvailableStaffWhenSlotIsNotHeld() {
        LocalDateTime startAt = DATE.atTime(TIME);
        LocalDateTime endAt = startAt.plusMinutes(service.getDurationMinutes());

        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service));
        when(staffServiceRepository.findAvailableStaffForService(
                SERVICE_ID,
                DATE,
                TIME,
                endAt.toLocalTime(),
                startAt,
                endAt,
                ServiceStatus.ACTIVE,
                UserStatus.ACTIVE,
                StaffServiceStatus.ACTIVE,
                StaffShiftStatus.APPROVED,
                BlockedSlotStatus.APPROVED,
                AppointmentStatus.CANCELLED))
                .thenReturn(List.of(staffService));
        when(appointmentHoldService.isSlotHeld(SERVICE_ID, STAFF_ID, startAt)).thenReturn(false);
        when(reviewsRepository.findAverageRatingsByStaffIds(anyList(), eq(ReviewsStatus.APPROVED)))
                .thenReturn(Map.of(STAFF_ID, 4.5D));
        when(appointmentDetailRepository.countCompletedServicesByStaffIds(anyList(), eq(AppointmentStatus.COMPLETED)))
                .thenReturn(Map.of(STAFF_ID, 3));
        when(staffServiceRepository.findSpecialtiesByStaffIds(
                anyList(),
                eq(StaffServiceStatus.ACTIVE),
                eq(ServiceStatus.ACTIVE)))
                .thenReturn(Map.of(STAFF_ID, List.of("Massage")));

        List<StaffServiceResponseDto> staff = handler.handle(
                new GetAvailableStaffForServiceQuery(SERVICE_ID, DATE, TIME));

        assertThat(staff).hasSize(1);
        assertThat(staff.getFirst().getId()).isEqualTo(STAFF_ID);
        assertThat(staff.getFirst().getStaffName()).isEqualTo("Nguyen An");
        assertThat(staff.getFirst().getRating()).isEqualTo(4.5D);
        assertThat(staff.getFirst().getCompletedServices()).isEqualTo(3);
        assertThat(staff.getFirst().getSpecialties()).containsExactly("Massage");
    }

    @Test
    void shouldExcludeAvailableStaffWhenSlotIsHeld() {
        LocalDateTime startAt = DATE.atTime(TIME);
        LocalDateTime endAt = startAt.plusMinutes(service.getDurationMinutes());

        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service));
        when(staffServiceRepository.findAvailableStaffForService(
                SERVICE_ID,
                DATE,
                TIME,
                endAt.toLocalTime(),
                startAt,
                endAt,
                ServiceStatus.ACTIVE,
                UserStatus.ACTIVE,
                StaffServiceStatus.ACTIVE,
                StaffShiftStatus.APPROVED,
                BlockedSlotStatus.APPROVED,
                AppointmentStatus.CANCELLED))
                .thenReturn(List.of(staffService));
        when(appointmentHoldService.isSlotHeld(SERVICE_ID, STAFF_ID, startAt)).thenReturn(true);

        List<StaffServiceResponseDto> staff = handler.handle(
                new GetAvailableStaffForServiceQuery(SERVICE_ID, DATE, TIME));

        assertThat(staff).isEmpty();
    }
}
