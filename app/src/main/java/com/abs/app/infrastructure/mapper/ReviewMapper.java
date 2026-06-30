package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.reviewmanager.dto.ReviewResponseDto;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.User;

public class ReviewMapper {

    public static ReviewResponseDto toReviewResponseDto(Reviews review) {
        String customerName = "Unknown";
        if (review.getAppointment() != null && review.getAppointment().getCustomer() != null) {
            User user = review.getAppointment().getCustomer();
            if (user.getFirstName() != null && user.getLastName() != null) {
                customerName = user.getFirstName() + " " + user.getLastName();
            } else if (user.getUserName() != null) {
                customerName = user.getUserName();
            }
        }

        return ReviewResponseDto.builder()
            .id(review.getId())
            .picture(review.getPicture())
            .description(review.getDescription())
            .serviceScore(review.getServiceScore())
            .status(review.getStatus())
            .createAt(review.getCreateAt())
            .customerName(customerName)
            .build();
    }
}
