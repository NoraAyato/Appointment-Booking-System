package com.abs.app.application.admin.staffservice.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStaffServiceListQuery {
    private String keyword;
    private String status;
    private int page;
    private int limit;
}
