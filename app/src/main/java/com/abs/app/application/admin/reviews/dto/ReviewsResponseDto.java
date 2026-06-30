package com.abs.app.application.admin.reviewmanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.abs.app.domain.entity.enums.ReviewStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private String id;
    private String picture;
    private String description;
    private BigDecimal serviceScore;
    private ReviewStatus status;
    private LocalDateTime createAt;
    private String customerName;

}
