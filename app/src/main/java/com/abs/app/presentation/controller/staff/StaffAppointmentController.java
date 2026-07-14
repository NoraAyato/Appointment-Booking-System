package com.abs.app.presentation.controller.staff;

import java.time.LocalDate;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.staff.appointment.command.CompleteStaffAppointmentCommand;
import com.abs.app.application.staff.appointment.command.CompleteStaffAppointmentCommandHandler;
import com.abs.app.application.staff.appointment.dto.CompleteStaffAppointmentRequestDto;
import com.abs.app.application.staff.appointment.dto.StaffAppointmentResponseDto;
import com.abs.app.application.staff.appointment.query.GetStaffAppointmentListQuery;
import com.abs.app.application.staff.appointment.query.GetStaffAppointmentListQueryHandler;
import com.abs.app.common.constant.StaffAppointmentConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
import com.abs.app.infrastructure.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/staff/appointments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffAppointmentController {
    private final GetStaffAppointmentListQueryHandler getStaffAppointmentListQueryHandler;
    private final CompleteStaffAppointmentCommandHandler completeStaffAppointmentCommandHandler;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StaffAppointmentResponseDto>>> getAppointments(
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        String staffId = SecurityUtils.getCurrentUserId();
        PageResponse<StaffAppointmentResponseDto> appointments = getStaffAppointmentListQueryHandler.handle(
                new GetStaffAppointmentListQuery(staffId, keyWord, date, status, page, limit));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StaffAppointmentConstant.GET_LIST_SUCCESS,
                appointments));
    }

    @PutMapping(value = "/{appointmentId}/complete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> completeAppointment(
            @PathVariable String appointmentId,
            @Valid @ModelAttribute CompleteStaffAppointmentRequestDto request) {
        String staffId = SecurityUtils.getCurrentUserId();

        completeStaffAppointmentCommandHandler.handle(new CompleteStaffAppointmentCommand(
                staffId,
                appointmentId,
                request.getPicture()));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StaffAppointmentConstant.COMPLETE_SUCCESS,
                null));
    }
}
