package com.abs.app.application.staff.appointment.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.common.constant.StaffAppointmentConstant;
import com.abs.app.common.constant.StaffShiftConstant;
import com.abs.app.common.constant.UserConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.repository.AppointmentDetailRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.file.FileStorageService;
import com.abs.app.domain.service.StaffAuthorizationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompleteStaffAppointmentCommandHandler {
    private final UserRepository userRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final FileStorageService fileStorageService;
    private final StaffAuthorizationService staffAuthorizationService;

    @Transactional
    public void handle(CompleteStaffAppointmentCommand command) {
        User staff = userRepository.findById(command.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException(UserConstant.USER_NOT_EXIST));
        staffAuthorizationService.ensureStaff(staff);

        AppointmentDetail appointmentDetail = appointmentDetailRepository
                .findByAppointmentIdAndStaffId(command.getAppointmentId(), command.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException(StaffAppointmentConstant.APPOINTMENT_NOT_FOUND));

        Appointment appointment = appointmentDetail.getAppointment();
        if (appointment == null) {
            throw new ResourceNotFoundException(StaffAppointmentConstant.APPOINTMENT_NOT_FOUND);
        }

        if (!AppointmentStatus.CONFIRMED.equals(appointment.getStatus())) {
            throw new BusinessException(StaffAppointmentConstant.ONLY_CONFIRMED_CAN_COMPLETE);
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        String storedPicture = fileStorageService.storeAppointment(command.getPicture(), command.getAppointmentId());
        appointmentDetail.setPicture(storedPicture);
        appointmentDetailRepository.save(appointmentDetail);
    }
}
