package com.abs.app.application.admin.blockedslot.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBlockedSlotQuery {
    private String keyWord;
    private String status;
    private int page;
    private int limit;
}
