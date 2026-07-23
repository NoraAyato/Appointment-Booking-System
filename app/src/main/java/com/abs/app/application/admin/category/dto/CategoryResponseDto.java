package com.abs.app.application.admin.category.dto;

import lombok.Data;

@Data
public class CategoryResponseDto {
    private String id;
    private String name;
    private String tagColor;
    private String description;
    private int totalService;
}
