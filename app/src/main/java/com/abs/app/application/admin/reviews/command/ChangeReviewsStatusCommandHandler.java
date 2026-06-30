package com.abs.app.application.admin.reviewmanager.command;

import com.abs.app.common.constant.ReviewConstant;
import com.abs.app.domain.entity.enums.ReviewStatus;
import com.abs.app.domain.service.ReviewsService;
import org.springframework.stereotype.Service;

import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChangeReviewStatusCommandHandler {

    private final ReviewRepository reviewRepository;
    private final ReviewsService reviewsService;
    public void handle(ChangeReviewStatusCommand command){
        Reviews reviews = reviewRepository.findById(command.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ReviewConstant.REVIEW_NOT_EXIST));
        ReviewStatus newStatus = reviewsService.handleReviewsStatus(command.getStatus().name());
        reviews.setStatus(newStatus);
        reviewRepository.save(reviews);
    }
}
