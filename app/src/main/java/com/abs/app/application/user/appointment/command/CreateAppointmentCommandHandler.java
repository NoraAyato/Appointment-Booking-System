package com.abs.app.application.user.appointment.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.appointment.dto.CreateAppointmentResponseDto;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.constant.StaffServiceConstant;
import com.abs.app.common.constant.AppointmentConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.util.GenerateIdUtil;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.AppointmentRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.AppointmentHoldService;
import com.abs.app.domain.service.AppointmentHoldService.AppointmentHold;
import com.abs.app.domain.service.DateTimeService;
import com.abs.app.domain.service.StaffAuthorizationService;
import com.abs.app.infrastructure.mapper.AppointmentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateAppointmentCommandHandler {
    private static final int DEFAULT_QUANTITY = 1;

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final StaffServiceRepository staffServiceRepository;
    private final UserRepository userRepository;
    private final AppointmentHoldService appointmentHoldService;
    private final DateTimeService dateTimeService;
    private final StaffAuthorizationService staffAuthorizationService;

    @Transactional
    public CreateAppointmentResponseDto handle(CreateAppointmentCommand command) {
        User customer = userRepository.findByIdWithRole(command.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        AppointmentHold appointmentHold = appointmentHoldService.findByToken(command.getHoldToken())
                .orElseThrow(() -> new BusinessException(AppointmentConstant.HOLD_NOT_FOUND));

        if (!command.getCustomerId().equals(appointmentHold.customerId())) {
            throw new BusinessException(AppointmentConstant.HOLD_NOT_OWNER);
        }

        ServiceEntity service = serviceRepository.findById(appointmentHold.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException(ServiceEntityConstant.NOT_EXIST));
        if (service.getStatus() != ServiceStatus.ACTIVE) {
            throw new BusinessException(ServiceEntityConstant.NOT_EXIST);
        }

        User staff = userRepository.findByIdWithRole(appointmentHold.staffId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        staffAuthorizationService.ensureStaff(staff);

        StaffService staffService = staffServiceRepository
                .findByStaffUserIdAndServiceId(appointmentHold.staffId(), appointmentHold.serviceId())
                .orElseThrow(() -> new BusinessException(StaffServiceConstant.STAFF_NOT_ASSIGNED_SERVICE));
        if (staffService.getStatus() != StaffServiceStatus.ACTIVE) {
            throw new BusinessException(StaffServiceConstant.STAFF_NOT_ASSIGNED_SERVICE);
        }

        LocalDateTime startAt = appointmentHold.startAt();
        LocalDateTime endAt = appointmentHold.endAt();
        if (!dateTimeService.isValidFutureDateTimeRange(startAt, endAt)) {
            throw new BusinessException(AppointmentConstant.INVALID_APPOINTMENT_TIME);
        }

        if (!isStaffAvailable(
                appointmentHold.serviceId(),
                appointmentHold.staffId(),
                startAt,
                endAt)) {
            throw new BusinessException(AppointmentConstant.STAFF_NOT_AVAILABLE);
        }

        Appointment appointment = new Appointment();
        appointment.setId(GenerateIdUtil.GenerateId(
                AppointmentConstant.SALT_TAG,
                AppointmentConstant.STRING_LIMIT));
        appointment.setCustomer(customer);
        appointment.setNote(command.getNote());
        appointment.setStatus(AppointmentStatus.PENDING);

        AppointmentDetail appointmentDetail = new AppointmentDetail();
        appointmentDetail.setAppointment(appointment);
        appointmentDetail.setService(service);
        appointmentDetail.setStaff(staff);
        appointmentDetail.setQuantity(DEFAULT_QUANTITY);
        appointmentDetail.setStartTime(startAt);
        appointmentDetail.setEndTime(endAt);
        appointment.getAppointmentDetails().add(appointmentDetail);

        Invoice invoice = new Invoice();
        invoice.setId(GenerateIdUtil.GenerateId(
                AppointmentConstant.INVOICE_SALT_TAG,
                AppointmentConstant.STRING_LIMIT));
        invoice.setAmount(calculateAmount(service, DEFAULT_QUANTITY));
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setAppointment(appointment);
        appointment.setInvoice(invoice);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        appointmentHoldService.invalidate(command.getHoldToken());

        return AppointmentMapper.toCreateAppointmentResponse(savedAppointment);
    }

    private boolean isStaffAvailable(
            String serviceId,
            String staffId,
            LocalDateTime startAt,
            LocalDateTime endAt) {
        return staffServiceRepository.findAvailableStaffForService(
                serviceId,
                startAt.toLocalDate(),
                startAt.toLocalTime(),
                endAt.toLocalTime(),
                startAt,
                endAt,
                ServiceStatus.ACTIVE,
                UserStatus.ACTIVE,
                StaffServiceStatus.ACTIVE,
                StaffShiftStatus.APPROVED,
                BlockedSlotStatus.APPROVED,
                AppointmentStatus.CANCELLED)
                .stream()
                .anyMatch(availableStaffService -> staffId
                        .equals(availableStaffService.getStaff().getUserId()));
    }

    private Double calculateAmount(ServiceEntity service, int quantity) {
        Double price = service.getPrice();
        return (price != null ? price : 0D) * quantity;
    }
}
