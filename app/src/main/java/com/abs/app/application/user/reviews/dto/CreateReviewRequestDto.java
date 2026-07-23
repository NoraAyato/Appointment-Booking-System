package com.abs.app.application.user.reviews.dto;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.abs.app.common.constant.ReviewsConstant;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequestDto {
    @NotNull(message = ReviewsConstant.VALID_SERVICE_SCORE_REQUIRED)
    @DecimalMin(value = "0.0", message = ReviewsConstant.VALID_SERVICE_SCORE_MIN)
    @DecimalMax(value = "5.0", message = ReviewsConstant.VALID_SERVICE_SCORE_MAX)
    private BigDecimal serviceScore;

    @NotBlank(message = ReviewsConstant.VALID_DESCRIPTION)
    private String description;

    private MultipartFile picture;
}
