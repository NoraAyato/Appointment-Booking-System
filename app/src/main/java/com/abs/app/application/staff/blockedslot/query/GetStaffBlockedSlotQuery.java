package com.abs.app.application.staff.blockedslot.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStaffBlockedSlotQuery {
    private String userId;
    private String keyWord;
    private String status;
    private int page;
    private int limit;
}
