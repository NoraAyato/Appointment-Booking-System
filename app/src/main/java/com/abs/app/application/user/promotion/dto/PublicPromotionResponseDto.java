package com.abs.app.application.user.promotion.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicPromotionResponseDto {
    private String id;
    private String code;
    private String description;
    private String discountLabel;
    private LocalDate startDate;
    private LocalDate endDate;
    private String image;
}
