package com.abs.app.application.user.promotion.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPublicPromotionsQuery {
    private int page;
    private int limit;
}
