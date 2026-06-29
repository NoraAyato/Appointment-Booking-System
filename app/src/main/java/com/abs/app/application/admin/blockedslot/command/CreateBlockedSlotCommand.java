package com.abs.app.application.admin.blockedslot.command;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlockedSlotCommand {
    private String userId;
    private String reason;
    private LocalDate blockedDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}
