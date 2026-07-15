package com.abs.app.unit.application.user.appointment.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abs.app.application.user.appointment.command.HoldAppointmentCommand;
import com.abs.app.application.user.appointment.command.HoldAppointmentCommandHandler;
import com.abs.app.application.user.appointment.dto.HoldAppointmentResponseDto;
import com.abs.app.common.constant.AppointmentConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.Role;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.AppointmentHoldService;
import com.abs.app.domain.service.AppointmentHoldService.AppointmentHold;
import com.abs.app.domain.service.DateTimeService;
import com.abs.app.domain.service.StaffAuthorizationService;

@ExtendWith(MockitoExtension.class)
class HoldAppointmentCommandHandlerTest {
        private static final String CUSTOMER_ID = "u_customer_001";
        private static final String STAFF_ID = "u_staff_001";
        private static final String SERVICE_ID = "ser_001";
        private static final LocalDate DATE = LocalDate.now().plusDays(1);
        private static final LocalTime TIME = LocalTime.of(9, 0);

        @Mock
        private ServiceRepository serviceRepository;

        @Mock
        private StaffServiceRepository staffServiceRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private AppointmentHoldService appointmentHoldService;

        @Mock
        private DateTimeService dateTimeService;

        @Mock
        private StaffAuthorizationService staffAuthorizationService;

        @InjectMocks
        private HoldAppointmentCommandHandler handler;

        private User customer;
        private User staff;
        private ServiceEntity service;
        private StaffService staffService;
        private HoldAppointmentCommand command;

        @BeforeEach
        void setup() {
                customer = buildUser(CUSTOMER_ID, RoleEnum.CUSTOMER);
                staff = buildUser(STAFF_ID, RoleEnum.STAFF);

                service = new ServiceEntity();
                service.setId(SERVICE_ID);
                service.setStatus(ServiceStatus.ACTIVE);
                service.setDurationMinutes(60);

                staffService = new StaffService();
                staffService.setStaff(staff);
                staffService.setService(service);
                staffService.setStatus(StaffServiceStatus.ACTIVE);

                command = new HoldAppointmentCommand(CUSTOMER_ID, SERVICE_ID, STAFF_ID, DATE, TIME);
        }

        @Test
        void shouldHoldAppointmentSlotWhenStaffIsAvailable() {
                LocalDateTime startAt = DATE.atTime(TIME);
                LocalDateTime endAt = startAt.plusMinutes(service.getDurationMinutes());

                stubRequiredEntities();
                when(dateTimeService.isValidFutureDateTimeRange(startAt, endAt)).thenReturn(true);
                stubAvailableStaff(List.of(staffService));
                when(appointmentHoldService.hold(any(AppointmentHold.class), anyLong()))
                                .thenReturn(true);

                HoldAppointmentResponseDto response = handler.handle(command);

                assertThat(response.getHoldToken()).isNotBlank();
                assertThat(response.getExpiresInSeconds()).isEqualTo(AppointmentConstant.HOLD_EXPIRATION_SECONDS);
                assertThat(response.getStartTime()).isEqualTo(startAt);
                assertThat(response.getEndTime()).isEqualTo(endAt);

                ArgumentCaptor<AppointmentHold> holdCaptor = ArgumentCaptor.forClass(AppointmentHold.class);
                verify(appointmentHoldService).hold(holdCaptor.capture(), anyLong());
                AppointmentHold appointmentHold = holdCaptor.getValue();

                assertThat(appointmentHold.customerId()).isEqualTo(CUSTOMER_ID);
                assertThat(appointmentHold.serviceId()).isEqualTo(SERVICE_ID);
                assertThat(appointmentHold.staffId()).isEqualTo(STAFF_ID);
                assertThat(appointmentHold.startAt()).isEqualTo(startAt);
                assertThat(appointmentHold.endAt()).isEqualTo(endAt);
        }

        @Test
        void shouldThrowBusinessExceptionWhenSlotAlreadyHeld() {
                LocalDateTime startAt = DATE.atTime(TIME);
                LocalDateTime endAt = startAt.plusMinutes(service.getDurationMinutes());

                stubRequiredEntities();
                when(dateTimeService.isValidFutureDateTimeRange(startAt, endAt)).thenReturn(true);
                stubAvailableStaff(List.of(staffService));
                when(appointmentHoldService.hold(any(AppointmentHold.class), anyLong()))
                                .thenReturn(false);

                assertThatThrownBy(() -> handler.handle(command))
                                .isInstanceOf(BusinessException.class);
        }

        @Test
        void shouldThrowBusinessExceptionWhenStaffIsNotAssignedToService() {
                when(userRepository.findByIdWithRole(CUSTOMER_ID)).thenReturn(Optional.of(customer));
                when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service));
                when(userRepository.findByIdWithRole(STAFF_ID)).thenReturn(Optional.of(staff));
                when(staffServiceRepository.findByStaffUserIdAndServiceId(STAFF_ID, SERVICE_ID))
                                .thenReturn(Optional.empty());

                assertThatThrownBy(() -> handler.handle(command))
                                .isInstanceOf(BusinessException.class);
        }

        private void stubRequiredEntities() {
                when(userRepository.findByIdWithRole(CUSTOMER_ID)).thenReturn(Optional.of(customer));
                when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service));
                when(userRepository.findByIdWithRole(STAFF_ID)).thenReturn(Optional.of(staff));
                when(staffServiceRepository.findByStaffUserIdAndServiceId(STAFF_ID, SERVICE_ID))
                                .thenReturn(Optional.of(staffService));
        }

        private void stubAvailableStaff(List<StaffService> staffServices) {
                when(staffServiceRepository.findAvailableStaffForService(
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any()))
                                .thenReturn(staffServices);
        }

        private User buildUser(String userId, RoleEnum roleEnum) {
                Role role = new Role();
                role.setRoleName(roleEnum);

                User user = new User();
                user.setUserId(userId);
                user.setRole(role);
                user.setStatus(UserStatus.ACTIVE);
                return user;
        }
}
