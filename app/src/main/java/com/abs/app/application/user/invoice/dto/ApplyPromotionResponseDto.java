package com.abs.app.application.user.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyPromotionResponseDto {
    private String promotionCode;
    private String discountValue;
}
