package com.abs.app.application.admin.promotion.dto;

import com.abs.app.common.constant.PromotionConstant;
import com.abs.app.domain.entity.enums.DiscountType;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UpdatePromotionRequestDto {
    @NotBlank(message = PromotionConstant.PROMOTION_DESCRIPTION_NOT_BLANK)
    private String description;

    @NotNull(message = PromotionConstant.PROMOTION_DISCOUNT_AMOUNT_NOT_NULL)
    @Positive(message = PromotionConstant.PROMOTION_DISCOUNT_AMOUNT_POSITIVE)
    private Double discountAmount;

    @NotNull(message = PromotionConstant.PROMOTION_DISCOUNT_TYPE_NOT_NULL)
    private String discountType;

    @NotBlank(message = PromotionConstant.PROMOTION_CODE_NOT_BLANK)
    @Size(min = 5, max = 20, message = PromotionConstant.PROMOTION_CODE_SIZE_UPDATE)
    private String promotionCode;

    @NotNull(message = PromotionConstant.PROMOTION_STATUS_NOT_NULL)
    private String status;

    private MultipartFile image;

    @NotNull(message = PromotionConstant.PROMOTION_START_DATE_NOT_NULL)
    private LocalDate startDate;

    @NotNull(message = PromotionConstant.PROMOTION_END_DATE_NOT_NULL)
    private LocalDate endDate;

}
