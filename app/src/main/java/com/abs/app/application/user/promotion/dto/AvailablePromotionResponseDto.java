package com.abs.app.application.user.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvailablePromotionResponseDto {
    private String promotionCode;
    private String description;
    private String discountType;
    private int value;
}
