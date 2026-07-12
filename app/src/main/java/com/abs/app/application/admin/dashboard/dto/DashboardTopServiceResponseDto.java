package com.abs.app.application.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardTopServiceResponseDto {
    private String serviceId;
    private String serviceName;
    private long bookingCount;
    private double revenue;
    private double averageRating;
}
