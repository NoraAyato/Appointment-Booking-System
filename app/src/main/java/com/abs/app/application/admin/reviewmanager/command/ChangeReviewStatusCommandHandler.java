package com.abs.app.application.admin.reviewmanager.command;

import org.springframework.stereotype.Service;

import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChangeReviewStatusCommandHandler {

    private final ReviewRepository reviewRepository;

    public void handle(ChangeReviewStatusCommand command) {
        Reviews review = reviewRepository.findById(command.getReviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + command.getReviewId()));
        
        if (command.getStatus() != null) {
            review.setStatus(command.getStatus());
            reviewRepository.save(review);
        }
    }
}
