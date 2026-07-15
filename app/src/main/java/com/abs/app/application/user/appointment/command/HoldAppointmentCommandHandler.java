package com.abs.app.application.user.appointment.command;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.abs.app.application.user.appointment.dto.HoldAppointmentResponseDto;
import com.abs.app.common.constant.ServiceEntityConstant;
import com.abs.app.common.constant.StaffServiceConstant;
import com.abs.app.common.constant.AppointmentConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffService;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.entity.enums.StaffServiceStatus;
import com.abs.app.domain.entity.enums.StaffShiftStatus;
import com.abs.app.domain.entity.enums.UserStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.repository.StaffServiceRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.AppointmentHoldService;
import com.abs.app.domain.service.AppointmentHoldService.AppointmentHold;
import com.abs.app.domain.service.DateTimeService;
import com.abs.app.domain.service.StaffAuthorizationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HoldAppointmentCommandHandler {
        private final ServiceRepository serviceRepository;
        private final StaffServiceRepository staffServiceRepository;
        private final UserRepository userRepository;
        private final AppointmentHoldService appointmentHoldService;
        private final DateTimeService dateTimeService;
        private final StaffAuthorizationService staffAuthorizationService;

        public HoldAppointmentResponseDto handle(HoldAppointmentCommand command) {
                userRepository.findByIdWithRole(command.getCustomerId())
                                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));

                ServiceEntity service = serviceRepository.findById(command.getServiceId())
                                .orElseThrow(() -> new ResourceNotFoundException(ServiceEntityConstant.NOT_EXIST));
                if (service.getStatus() != ServiceStatus.ACTIVE) {
                        throw new BusinessException(ServiceEntityConstant.NOT_EXIST);
                }

                User staff = userRepository.findByIdWithRole(command.getStaffId())
                                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
                staffAuthorizationService.ensureStaff(staff);

                StaffService staffService = staffServiceRepository
                                .findByStaffUserIdAndServiceId(command.getStaffId(), command.getServiceId())
                                .orElseThrow(() -> new BusinessException(
                                                StaffServiceConstant.STAFF_NOT_ASSIGNED_SERVICE));
                if (staffService.getStatus() != StaffServiceStatus.ACTIVE) {
                        throw new BusinessException(StaffServiceConstant.STAFF_NOT_ASSIGNED_SERVICE);
                }

                LocalDateTime startAt = command.getDate().atTime(command.getTime());
                LocalDateTime endAt = startAt.plusMinutes(service.getDurationMinutes());
                if (!dateTimeService.isValidFutureDateTimeRange(startAt, endAt)) {
                        throw new BusinessException(AppointmentConstant.INVALID_APPOINTMENT_TIME);
                }

                if (!isStaffAvailable(
                                command.getServiceId(),
                                command.getStaffId(),
                                startAt,
                                endAt)) {
                        throw new BusinessException(AppointmentConstant.STAFF_NOT_AVAILABLE);
                }

                AppointmentHold appointmentHold = new AppointmentHold(
                                UUID.randomUUID().toString(),
                                command.getCustomerId(),
                                command.getServiceId(),
                                command.getStaffId(),
                                startAt,
                                endAt);

                boolean held = appointmentHoldService.hold(
                                appointmentHold,
                                AppointmentConstant.HOLD_EXPIRATION_MINUTES);
                if (!held) {
                        throw new BusinessException(AppointmentConstant.SLOT_ALREADY_HELD);
                }

                return new HoldAppointmentResponseDto(
                                appointmentHold.holdToken(),
                                AppointmentConstant.HOLD_EXPIRATION_SECONDS,
                                startAt,
                                endAt);
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
}
