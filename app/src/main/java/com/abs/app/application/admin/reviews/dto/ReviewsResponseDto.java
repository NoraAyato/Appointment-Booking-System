package com.abs.app.application.admin.reviews.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.abs.app.domain.entity.enums.ReviewsStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewsResponseDto {
    private String id;
    private String picture;
    private String description;
    private BigDecimal serviceScore;
    private ReviewsStatus status;
    private LocalDateTime createAt;
    private String customerName;
}
