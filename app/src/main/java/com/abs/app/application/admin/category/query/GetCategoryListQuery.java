package com.abs.app.application.admin.category.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoryListQuery {
    private String keyword;
    private int page;
    private int size;
}
