package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.reviews.dto.ReviewsResponseDto;
import com.abs.app.domain.entity.Reviews;

public class ReviewsMapper {
    public static ReviewsResponseDto toAdminReviewResponseDto(Reviews reviews){
        ReviewsResponseDto responseDto = new ReviewsResponseDto();
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
