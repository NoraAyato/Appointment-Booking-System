package com.abs.app.application.user.invoice.dto;

import com.abs.app.common.constant.InvoiceConstant;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplyPromotionRequestDto {
    @NotBlank(message = InvoiceConstant.PROMOTION_CODE_NOT_BLANK)
    private String promotionCode;
}
