package com.abs.app.application.user.reviews.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.abs.app.common.constant.AppointmentConstant;
import com.abs.app.common.constant.ReviewsConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.util.GenerateIdUtil;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.repository.AppointmentRepository;
import com.abs.app.domain.repository.ReviewsRepository;
import com.abs.app.infrastructure.file.FileStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateReviewCommandHandler {
    private final AppointmentRepository appointmentRepository;
    private final ReviewsRepository reviewsRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public void handle(CreateReviewCommand command) {
        Appointment appointment = appointmentRepository.findById(command.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(AppointmentConstant.APPOINTMENT_NOT_EXIST));

        if (!appointment.getCustomer().getUserId().equals(command.getCustomerId())) {
            throw new BusinessException(ReviewsConstant.INVALID_REVIEW);
        }

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new BusinessException(ReviewsConstant.ONLY_COMPLETED_CAN_REVIEW);
        }

        if (appointment.getReviews() != null) {
            throw new BusinessException(ReviewsConstant.REVIEW_ALREADY_EXISTS);
        }

        String reviewId = GenerateIdUtil.GenerateId(ReviewsConstant.SALT_TAG, ReviewsConstant.STRING_LIMIT);

        String storedPicture = null;
        MultipartFile picture = command.getPicture();
        if (picture != null && !picture.isEmpty()) {
            storedPicture = fileStorageService.storeReview(picture, reviewId);
        }

        Reviews review = new Reviews();
        review.setId(reviewId);
        review.setAppointment(appointment);
        review.setServiceScore(command.getServiceScore());
        review.setDescription(command.getDescription());
        review.setPicture(storedPicture);
        review.setCreateAt(LocalDateTime.now());
        review.setStatus(ReviewsStatus.PENDING);
        reviewsRepository.save(review);
    }
}
