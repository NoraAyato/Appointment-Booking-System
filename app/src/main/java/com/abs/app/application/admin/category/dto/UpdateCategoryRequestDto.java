package com.abs.app.application.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequestDto {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    private String description;
}
