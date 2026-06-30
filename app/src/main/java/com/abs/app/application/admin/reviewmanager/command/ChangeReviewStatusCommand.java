package com.abs.app.application.admin.reviewmanager.command;

import com.abs.app.domain.entity.enums.ReviewStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeReviewStatusCommand {
    private String reviewId;
    private ReviewStatus status;
}
