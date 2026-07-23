package com.abs.app.unit.application.user.appointment.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

import com.abs.app.application.user.appointment.command.CreateAppointmentCommand;
import com.abs.app.application.user.appointment.command.CreateAppointmentCommandHandler;
import com.abs.app.application.user.appointment.dto.CreateAppointmentResponseDto;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.Role;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.AppointmentRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.AppointmentHoldService;
import com.abs.app.domain.service.AppointmentHoldService.AppointmentHold;
import com.abs.app.domain.service.DateTimeService;
import com.abs.app.domain.service.StaffAuthorizationService;

@ExtendWith(MockitoExtension.class)
class CreateAppointmentCommandHandlerTest {
    private static final String CUSTOMER_ID = "u_customer_001";
    private static final String STAFF_ID = "u_staff_001";
    private static final String SERVICE_ID = "ser_001";
    private static final String HOLD_TOKEN = "hold-token-001";
    private static final String NOTE = "Need a quiet room";
    private static final LocalDate DATE = LocalDate.now().plusDays(1);
    private static final LocalTime TIME = LocalTime.of(9, 0);

    @Mock
    private AppointmentRepository appointmentRepository;

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
    private CreateAppointmentCommandHandler handler;

    private User customer;
    private User staff;
    private ServiceEntity service;
    private StaffService staffService;
    private CreateAppointmentCommand command;
    private AppointmentHold appointmentHold;

    @BeforeEach
    void setup() {
        customer = buildUser(CUSTOMER_ID, RoleEnum.CUSTOMER);
        staff = buildUser(STAFF_ID, RoleEnum.STAFF);

        service = new ServiceEntity();
        service.setId(SERVICE_ID);
        service.setStatus(ServiceStatus.ACTIVE);
        service.setDurationMinutes(60);
        service.setPrice(250000D);

        staffService = new StaffService();
        staffService.setStaff(staff);
        staffService.setService(service);
        staffService.setStatus(StaffServiceStatus.ACTIVE);

        LocalDateTime startAt = DATE.atTime(TIME);
        LocalDateTime endAt = startAt.plusMinutes(service.getDurationMinutes());
        appointmentHold = new AppointmentHold(HOLD_TOKEN, CUSTOMER_ID, SERVICE_ID, STAFF_ID, startAt, endAt);

        command = new CreateAppointmentCommand(CUSTOMER_ID, HOLD_TOKEN, NOTE);
    }

    @Test
    void shouldCreateAppointmentWhenStaffIsAvailable() {
        LocalDateTime startAt = DATE.atTime(TIME);
        LocalDateTime endAt = startAt.plusMinutes(service.getDurationMinutes());

        stubRequiredEntities();
        when(dateTimeService.isValidFutureDateTimeRange(startAt, endAt)).thenReturn(true);
        stubAvailableStaff(List.of(staffService));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment appointment = invocation.getArgument(0);
            appointment.getAppointmentDetails().get(0).setId(1L);
            return appointment;
        });

        CreateAppointmentResponseDto response = handler.handle(command);

        assertThat(response.getInvoiceId()).startsWith("inv_");

        ArgumentCaptor<Appointment> appointmentCaptor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(appointmentCaptor.capture());
        Appointment savedAppointment = appointmentCaptor.getValue();

        assertThat(savedAppointment.getCustomer()).isSameAs(customer);
        assertThat(savedAppointment.getNote()).isEqualTo(NOTE);
        assertThat(savedAppointment.getStatus()).isEqualTo(AppointmentStatus.PENDING);
        assertThat(savedAppointment.getAppointmentDetails()).hasSize(1);
        assertThat(savedAppointment.getAppointmentDetails().get(0).getAppointment()).isSameAs(savedAppointment);
        assertThat(savedAppointment.getInvoice()).isNotNull();
        assertThat(savedAppointment.getInvoice().getId()).startsWith("inv_");
        assertThat(savedAppointment.getInvoice().getAmount()).isEqualTo(250000D);
        assertThat(savedAppointment.getInvoice().getStatus()).isEqualTo(InvoiceStatus.UNPAID);
        assertThat(savedAppointment.getInvoice().getAppointment()).isSameAs(savedAppointment);
        verify(appointmentHoldService).invalidate(HOLD_TOKEN);
    }

    @Test
    void shouldThrowBusinessExceptionWhenStaffIsNotAvailable() {
        LocalDateTime startAt = DATE.atTime(TIME);
        LocalDateTime endAt = startAt.plusMinutes(service.getDurationMinutes());

        stubRequiredEntities();
        when(dateTimeService.isValidFutureDateTimeRange(startAt, endAt)).thenReturn(true);
        stubAvailableStaff(List.of());

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(BusinessException.class);

        verify(appointmentRepository, never()).save(any(Appointment.class));
        verify(appointmentHoldService, never()).invalidate(HOLD_TOKEN);
    }

    private void stubRequiredEntities() {
        when(userRepository.findByIdWithRole(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(appointmentHoldService.findByToken(HOLD_TOKEN)).thenReturn(Optional.of(appointmentHold));
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
