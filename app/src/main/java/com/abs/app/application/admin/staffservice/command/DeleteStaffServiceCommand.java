package com.abs.app.application.admin.staffservice.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteStaffServiceCommand {
    private String staffId;
    private String serviceId;
}
