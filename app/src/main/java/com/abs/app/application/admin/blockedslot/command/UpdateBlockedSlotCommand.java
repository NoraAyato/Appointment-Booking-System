package com.abs.app.application.admin.blockedslot.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlockedSlotCommand {
    private Long id;
    private String status;
}
