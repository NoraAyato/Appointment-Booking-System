package com.abs.app.presentation.controller.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.appointment.command.CreateAppointmentCommand;
import com.abs.app.application.user.appointment.command.CreateAppointmentCommandHandler;
import com.abs.app.application.user.appointment.command.HoldAppointmentCommand;
import com.abs.app.application.user.appointment.command.HoldAppointmentCommandHandler;
import com.abs.app.application.user.appointment.dto.BookingHistoryResponseDto;
import com.abs.app.application.user.appointment.dto.CreateAppointmentRequestDto;
import com.abs.app.application.user.appointment.dto.CreateAppointmentResponseDto;
import com.abs.app.application.user.appointment.dto.HoldAppointmentRequestDto;
import com.abs.app.application.user.appointment.dto.HoldAppointmentResponseDto;
import com.abs.app.application.user.appointment.query.GetBookingHistoryQuery;
import com.abs.app.application.user.appointment.query.GetBookingHistoryQueryHandler;
import com.abs.app.application.user.reviews.command.CreateReviewCommand;
import com.abs.app.application.user.reviews.command.CreateReviewCommandHandler;
import com.abs.app.application.user.reviews.dto.CreateReviewRequestDto;
import com.abs.app.common.constant.AppointmentConstant;
import com.abs.app.common.constant.ReviewsConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.common.response.PageResponse;
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
        private final GetBookingHistoryQueryHandler getBookingHistoryQueryHandler;
        private final CreateReviewCommandHandler createReviewCommandHandler;

        @GetMapping("/history")
        public ResponseEntity<ApiResponse<PageResponse<BookingHistoryResponseDto>>> getBookingHistory(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int limit) {
                String customerId = SecurityUtils.getCurrentUserId();
                PageResponse<BookingHistoryResponseDto> bookingHistory = getBookingHistoryQueryHandler.handle(
                                new GetBookingHistoryQuery(customerId, page, limit));

                return ResponseEntity.ok(new ApiResponse<>(
                                true,
                                AppointmentConstant.GET_HISTORY_SUCCESS,
                                bookingHistory));
        }

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

        @PostMapping(value = "/{appointmentId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<Void>> createReview(
                        @PathVariable String appointmentId,
                        @Valid @ModelAttribute CreateReviewRequestDto request) {
                String customerId = SecurityUtils.getCurrentUserId();

                createReviewCommandHandler.handle(new CreateReviewCommand(
                                customerId,
                                appointmentId,
                                request.getServiceScore(),
                                request.getDescription(),
                                request.getPicture()));

                return ResponseEntity.ok(new ApiResponse<>(
                                true,
                                ReviewsConstant.CREATE_REVIEW_SUCCESS,
                                null));
        }
}
