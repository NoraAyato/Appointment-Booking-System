package com.abs.app.application.staff.staffshift.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStaffShiftListQuery {
    private String userId;
    private String keyWord;
    private String status;
    private int page;
    private int limit;
}
