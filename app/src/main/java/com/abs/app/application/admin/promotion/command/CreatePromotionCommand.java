package com.abs.app.application.admin.promotion.command;

import com.abs.app.domain.entity.enums.DiscountType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class CreatePromotionCommand {
    private final String promotionCode;
    private final String description;
    private final Double discountAmount;
    private final String discountType;
    private final MultipartFile image;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String userId;
}