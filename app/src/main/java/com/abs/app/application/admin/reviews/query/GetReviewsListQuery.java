package com.abs.app.application.admin.reviews.query;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetReviewsListQuery {
    private String keyWord;
    private String status;
    private int page;
    private int limit;
}
