package com.abs.app.application.admin.staffshift.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStaffShiftListQuery {
    private String keyWord;
    private String status;
    private int page;
    private int limit;
}
