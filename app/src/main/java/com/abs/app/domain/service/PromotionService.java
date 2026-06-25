package com.abs.app.domain.service;

import java.time.LocalDate;
import java.util.List;

import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.DiscountType;
import com.abs.app.domain.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import com.abs.app.common.constant.PromotionConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.enums.PromotionStatus;

@Service
public class PromotionService {
    private PromotionRepository promotionRepository;
    public PromotionStatus convertPromotionStatusToEnum(String status) {
        return switch (status) {
            case "ACTIVE" -> PromotionStatus.ACTIVE;
            case "INACTIVE" -> PromotionStatus.INACTIVE;
            case "DELETED" -> PromotionStatus.DELETED;
            default -> throw new BusinessException(PromotionConstant.INVALID_PROMOTION_STATUS);
        };
    }

    public boolean isInvalidPromotionDate(LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(endDate);
    }

    public boolean isPromotionDateOverlapped(List<Promotion> promotionList, LocalDate startDate, LocalDate endDate) {
        return promotionList.stream().anyMatch(oldPromotion ->
                !oldPromotion.getStartDate().isAfter(endDate) &&
                        !oldPromotion.getEndDate().isBefore(startDate));

    }
    

    public DiscountType convertDiscountTypeStringToEnum(String discountType) {
        return switch (discountType) {
            case "PERCENTAGE" -> DiscountType.PERCENTAGE;
            case "FIXED_AMOUNT" -> DiscountType.FIXED_AMOUNT;
            default -> throw new BusinessException(PromotionConstant.INVALID_PROMOTION_STATUS);
        };
    }
}
