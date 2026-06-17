package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.domain.entity.Promotion;

public class PromotionMapper {
    public static PromotionResponseDto toPromotionResponse(Promotion promotion) {
        PromotionResponseDto dto = new PromotionResponseDto();
        dto.setId(promotion.getId());
        dto.setDescription(promotion.getDescription());
        dto.setDiscountAmount(promotion.getDiscountAmount());
        dto.setDiscountType(promotion.getDiscountType());
        dto.setActive(promotion.getActive());
        dto.setImage(promotion.getImage());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        return dto;
    }
}
