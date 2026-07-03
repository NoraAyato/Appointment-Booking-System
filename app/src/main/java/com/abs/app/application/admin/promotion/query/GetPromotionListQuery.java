package com.abs.app.application.admin.promotion.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPromotionListQuery {
    private String keyword;
    private String status;
    private String discountType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int page;
    private int size;
}
