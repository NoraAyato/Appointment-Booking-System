package com.abs.app.application.admin.reviewmanager.command;

import com.abs.app.domain.entity.enums.ReviewStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeReviewStatusCommand {
    private String id;
    private ReviewStatus status;
}
