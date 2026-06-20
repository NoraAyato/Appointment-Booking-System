package com.abs.app.application.admin.promotion.dto;

import com.abs.app.domain.entity.enums.DiscountType;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class CreatePromotionRequestDto {

    @NotBlank
    @NotNull
    @Size(min = 6, max = 13)
    private String promotionCode;

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double discountAmount;

    @NotNull
    private String discountType;

    private MultipartFile image;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
