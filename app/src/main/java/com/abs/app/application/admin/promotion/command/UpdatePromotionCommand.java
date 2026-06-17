package com.abs.app.application.admin.promotion.command;

import com.abs.app.domain.entity.enums.DiscountType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UpdatePromotionCommand {
    private final String id;
    private final String description;
    private final Double discountAmount;
    private final DiscountType discountType;
    private final Boolean active;
    private final MultipartFile image;
    private final LocalDate startDate;
    private final LocalDate endDate;
}
