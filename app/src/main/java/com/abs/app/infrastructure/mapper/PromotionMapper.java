package com.abs.app.infrastructure.mapper;

import com.abs.app.application.admin.promotion.dto.PromotionResponseDto;
import com.abs.app.application.user.promotion.dto.AvailablePromotionResponseDto;
import com.abs.app.application.user.promotion.dto.PublicPromotionResponseDto;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.enums.DiscountType;

public class PromotionMapper {
    public static PromotionResponseDto toPromotionResponse(Promotion promotion) {
        PromotionResponseDto dto = new PromotionResponseDto();
        dto.setId(promotion.getId());
        dto.setPromotionCode(promotion.getCode());
        dto.setDescription(promotion.getDescription());
        dto.setDiscountAmount(promotion.getDiscountAmount());
        dto.setDiscountType(promotion.getDiscountType().toString());
        dto.setStatus(promotion.getStatus().toString());
        dto.setImage(promotion.getImage());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        return dto;
    }

    public static AvailablePromotionResponseDto toAvailablePromotionResponse(Promotion promotion) {
        AvailablePromotionResponseDto dto = new AvailablePromotionResponseDto();
        dto.setPromotionCode(promotion.getCode());
        dto.setDescription(promotion.getDescription());
        dto.setDiscountType(promotion.getDiscountType().toString());
        dto.setValue(promotion.getDiscountAmount().intValue());
        return dto;
    }

    public static PublicPromotionResponseDto toPublicPromotionResponse(Promotion promotion) {
        PublicPromotionResponseDto dto = new PublicPromotionResponseDto();
        dto.setId(promotion.getId());
        dto.setCode(promotion.getCode());
        dto.setDescription(promotion.getDescription());
        dto.setDiscountLabel(toDiscountLabel(promotion));
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setImage(promotion.getImage());
        return dto;
    }

    private static String toDiscountLabel(Promotion promotion) {
        if (promotion.getDiscountType() == DiscountType.PERCENTAGE) {
            return promotion.getDiscountAmount().intValue() + "%";
        }

        return promotion.getDiscountAmount().intValue() + " VND";
    }
}
