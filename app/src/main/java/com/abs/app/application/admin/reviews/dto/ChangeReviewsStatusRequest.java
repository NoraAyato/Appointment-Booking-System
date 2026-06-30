package com.abs.app.application.admin.reviewmanager.dto;

import com.abs.app.domain.entity.enums.ReviewStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeReviewStatusRequest {
    @NotBlank
    @NotNull
    private ReviewStatus status;
}
