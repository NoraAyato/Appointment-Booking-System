package com.abs.app.application.admin.promotion.dto;

import com.abs.app.domain.entity.enums.DiscountType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PromotionResponseDto {
    private String id;
    private String description;
    private Double discountAmount;
    private DiscountType discountType;
    private String status;
    private String image;
    private LocalDate startDate;
    private LocalDate endDate;
    private String userId;
}
