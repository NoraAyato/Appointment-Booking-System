package com.abs.app.application.admin.service.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetServiceListQuery {
    private String keyword;
    private int page;
    private int size;
}
