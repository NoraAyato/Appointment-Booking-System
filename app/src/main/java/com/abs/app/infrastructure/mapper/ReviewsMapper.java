package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.reviewmanager.dto.ReviewResponseDto;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.User;

public class ReviewMapper {
    public static ReviewResponseDto toAdminReviewResponseDto(Reviews reviews){
        ReviewResponseDto responseDto = new ReviewResponseDto();
        responseDto.setId(reviews.getId());
        responseDto.setPicture(reviews.getPicture());
        responseDto.setDescription(reviews.getDescription());
        responseDto.setServiceScore(reviews.getServiceScore());
        responseDto.setCreateAt(reviews.getCreateAt());
        responseDto.setStatus(reviews.getStatus());
        java.lang.String customerName = reviews.getAppointment().getCustomer().getFirstName()+ " " +reviews.getAppointment().getCustomer().getLastName();
        responseDto.setCustomerName(customerName);
        return responseDto;
    }
}
