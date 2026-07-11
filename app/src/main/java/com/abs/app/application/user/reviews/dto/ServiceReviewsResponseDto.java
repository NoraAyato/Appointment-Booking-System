package com.abs.app.application.user.reviews.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceReviewsResponseDto {
    private String id;
    private String customerName;
    private String customerAvatar;
    private String imageUrl;
    private String content;
    private double rating;
    private LocalDate createdAt;
    private String staffName;
    private LocalDate serviceDate;
}
