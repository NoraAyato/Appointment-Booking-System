package com.abs.app.application.admin.usermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsResponseDto {
    private int totalUsers;
    private int activeUsers;
    private int inactiveUsers;
}
