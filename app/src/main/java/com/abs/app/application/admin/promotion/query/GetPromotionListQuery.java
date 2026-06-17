package com.abs.app.application.admin.promotion.query;

import lombok.Data;
import java.time.LocalDate;

@Data
public class GetPromotionListQuery {
    private final int page;
    private final int size;
    private final String keyword;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Boolean active;
}
