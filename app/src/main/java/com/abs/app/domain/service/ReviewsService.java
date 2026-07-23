package com.abs.app.domain.service;

import com.abs.app.common.constant.ReviewsConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import org.springframework.stereotype.Service;

@Service
public class ReviewsService {
    public ReviewsStatus handleReviewsStatus(String status){
        return switch (status) {
            case "PENDING" -> ReviewsStatus.PENDING;
            case "APPROVED" -> ReviewsStatus.APPROVED;
            case "DROPPED" -> ReviewsStatus.DROPPED;
            default -> throw new BusinessException(ReviewsConstant.INVALID_REVIEW_SLOT_STATUS);
        };
    }
}
