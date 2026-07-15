package com.abs.app.presentation.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.appointment.command.CreateAppointmentCommand;
import com.abs.app.application.user.appointment.command.CreateAppointmentCommandHandler;
import com.abs.app.application.user.appointment.command.HoldAppointmentCommand;
import com.abs.app.application.user.appointment.command.HoldAppointmentCommandHandler;
import com.abs.app.application.user.appointment.dto.CreateAppointmentRequestDto;
import com.abs.app.application.user.appointment.dto.CreateAppointmentResponseDto;
import com.abs.app.application.user.appointment.dto.HoldAppointmentRequestDto;
import com.abs.app.application.user.appointment.dto.HoldAppointmentResponseDto;
import com.abs.app.common.constant.AuthConstant;
import com.abs.app.common.constant.AppointmentConstant;
import com.abs.app.common.exception.UnauthorizedException;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.infrastructure.security.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AppointmentController {
        private final CreateAppointmentCommandHandler createAppointmentCommandHandler;
        private final HoldAppointmentCommandHandler holdAppointmentCommandHandler;

        @PostMapping("/holds")
        public ResponseEntity<ApiResponse<HoldAppointmentResponseDto>> holdAppointment(
                        @Valid @RequestBody HoldAppointmentRequestDto request) {
                String customerId = SecurityUtils.getCurrentUserId();

                HoldAppointmentResponseDto appointmentHold = holdAppointmentCommandHandler.handle(
                                new HoldAppointmentCommand(
                                                customerId,
                                                request.getServiceId(),
                                                request.getStaffId(),
                                                request.getDate(),
                                                request.getTime()));

                return ResponseEntity.ok(new ApiResponse<>(
                                true,
                                AppointmentConstant.HOLD_SUCCESS,
                                appointmentHold));
        }

        @PostMapping
        public ResponseEntity<ApiResponse<CreateAppointmentResponseDto>> createAppointment(
                        @Valid @RequestBody CreateAppointmentRequestDto request) {
                String customerId = SecurityUtils.getCurrentUserId();

                CreateAppointmentResponseDto appointment = createAppointmentCommandHandler.handle(
                                new CreateAppointmentCommand(
                                                customerId,
                                                request.getHoldToken(),
                                                request.getNote()));

                return ResponseEntity.ok(new ApiResponse<>(
                                true,
                                AppointmentConstant.CREATE_SUCCESS,
                                appointment));
        }
}
