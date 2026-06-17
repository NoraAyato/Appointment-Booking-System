package com.abs.app.application.admin.promotion.dto;

import com.abs.app.domain.entity.enums.DiscountType;
<<<<<<< HEAD
=======
import jakarta.validation.constraints.*;
>>>>>>> 2f17173 (feat: Fix Promotion)
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class CreatePromotionRequestDto {
    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double discountAmount;

    @NotNull
    private DiscountType discountType;

    @NotNull
    private Boolean active;

    private MultipartFile image;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String userId;
}
