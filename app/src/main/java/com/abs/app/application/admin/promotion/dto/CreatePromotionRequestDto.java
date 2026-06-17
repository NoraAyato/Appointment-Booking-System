package com.abs.app.application.admin.promotion.dto;

import com.abs.app.domain.entity.enums.DiscountType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class CreatePromotionRequestDto {
    private String description;
    private Double discountAmount;
    private DiscountType discountType;
    private Boolean active;
    private MultipartFile image;
    private LocalDate startDate;
    private LocalDate endDate;
}
