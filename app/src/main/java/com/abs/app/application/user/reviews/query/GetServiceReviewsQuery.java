package com.abs.app.application.user.reviews.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetServiceReviewsQuery {
    private String serviceId;
    private int page;
    private int limit;
}
