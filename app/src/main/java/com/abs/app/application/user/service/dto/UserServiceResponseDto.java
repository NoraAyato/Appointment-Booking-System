package com.abs.app.application.user.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserServiceResponseDto {
    private String id;
    private String name;
    private String description;
    private int durationMinutes;
    private double price;
    private double rating;
    private String category;
    private String categoryTagColor;
    private List<String> images;
}
