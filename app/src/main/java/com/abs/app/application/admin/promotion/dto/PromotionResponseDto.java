package com.abs.app.application.admin.promotion.dto;

import com.abs.app.domain.entity.enums.DiscountType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PromotionResponseDto {
    private String id;
    private String description;
    private Double discountAmount;
    private DiscountType discountType;
    private Boolean active;
    private String image;
    private LocalDate startDate;
    private LocalDate endDate;
}
