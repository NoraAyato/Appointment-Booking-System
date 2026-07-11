package com.abs.app.application.user.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopReviewsResponseDto {
    private String id;
    private String customerName;
    private String customerAvatar;
    private String serviceName;
    private String imageUrl;
    private String content;
    private double rating;
    private String serviceDate;
}
