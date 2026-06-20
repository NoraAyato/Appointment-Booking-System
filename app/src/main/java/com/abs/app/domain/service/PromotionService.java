package com.abs.app.domain.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.PromotionConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.enums.PromotionStatus;

@Service
public class PromotionService {
    public PromotionStatus handlePromotionStatus(String status) {
        PromotionStatus result = null;
        switch (status) {
            case "ACTIVE":
                result = PromotionStatus.ACTIVE;
                break;
            case "INACTIVE":
                result = PromotionStatus.INACTIVE;
                break;
            case "DELETED":
                result = PromotionStatus.DELETED;
                break;
            default:
                throw new BusinessException(PromotionConstant.INVALID_PROMOTION_STATUS);
        }
        return result;
    }

    public boolean handlePromotionDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
            return false;
        }
        return false;
    }
}
