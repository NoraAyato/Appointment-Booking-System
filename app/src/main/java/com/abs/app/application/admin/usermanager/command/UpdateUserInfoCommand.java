package com.abs.app.application.admin.usermanager.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoCommand {
    private String userId;
    private String role;
    private String status;
}
