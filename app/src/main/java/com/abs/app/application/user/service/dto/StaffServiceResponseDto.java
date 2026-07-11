package com.abs.app.application.user.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaffServiceResponseDto {
    private String id;
    private String staffName;
    private String staffAvatar;
    private double rating;
    private int completedServices;
    private List<String> specialties;
}
