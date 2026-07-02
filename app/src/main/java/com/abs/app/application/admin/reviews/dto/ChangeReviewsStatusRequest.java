package com.abs.app.application.admin.reviews.dto;

import com.abs.app.domain.entity.enums.ReviewsStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeReviewsStatusRequest {
    @NotBlank
    @NotNull
    private ReviewsStatus status;
}
