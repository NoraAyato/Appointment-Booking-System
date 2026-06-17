package com.abs.app.application.admin.promotion.query;

import lombok.Data;

@Data
public class GetPromotionListQuery {
    private final int page;
    private final int size;
    private final String keyword;
}
