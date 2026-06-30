package com.abs.app.application.admin.reviewmanager.query;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetReviewListQuery {
    private String keyWord;
    private String status;
    private int page;
    private int limit;
}
