package com.abs.app.application.admin.reviews.command;

import com.abs.app.common.constant.ReviewsConstant;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import com.abs.app.domain.service.ReviewsService;
import org.springframework.stereotype.Service;

import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.repository.ReviewsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChangeReviewsStatusCommandHandler {

    private final ReviewsRepository reviewRepository;
    private final ReviewsService reviewsService;
    public void handle(ChangeReviewsStatusCommand command){
        Reviews reviews = reviewRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ReviewsConstant.REVIEW_NOT_EXIST));
        ReviewsStatus newStatus = reviewsService.handleReviewsStatus(command.getStatus().name());
        reviews.setStatus(newStatus);
        reviewRepository.save(reviews);
    }
}
