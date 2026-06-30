package com.abs.app.application.admin.reviewmanager.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewListQuery {
    private int page;
    private int limit;
    private String keyword;
    private String status;
}
