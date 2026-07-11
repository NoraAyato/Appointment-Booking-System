package com.abs.app.application.user.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RatingDistribution {
    private int rating;
    private int count;
    private double percentage;
}
