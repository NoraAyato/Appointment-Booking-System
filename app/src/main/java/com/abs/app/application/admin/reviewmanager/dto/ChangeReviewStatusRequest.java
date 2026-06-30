package com.abs.app.application.admin.reviewmanager.dto;

import com.abs.app.domain.entity.enums.ReviewStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeReviewStatusRequest {
    private ReviewStatus status;
}
