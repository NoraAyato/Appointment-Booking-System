package com.abs.app.application.admin.reviews.command;

import com.abs.app.domain.entity.enums.ReviewsStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeReviewsStatusCommand {
    private String id;
    private ReviewsStatus status;
}
